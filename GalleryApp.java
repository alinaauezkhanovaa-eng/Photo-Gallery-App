package UI;

import Builder.ExportSettings;
import Builder.ExportSettingsBuilder;
import Decorator.ImageComponent;
import Facade.GalleryFacade;
import Factory.MediaFile;
import Model.FilterType;
import Model.MediaType;
import Model.Photo;
import Observer.AuditLogger;
import Strategy.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class GalleryApp extends JPanel {
    private static class Item {
        MediaFile media;Photo photo;double sizeMb;ImageComponent image;String filters="";
    }
    private final GalleryFacade gallery=new GalleryFacade(200);
    private final List<Photo> photos=new ArrayList<>();
    private final Map<Photo, Item> items=new IdentityHashMap<>();
    private final DefaultListModel<Photo> listModel=new DefaultListModel<>();
    private final JList<Photo> list=new JList<>(listModel);
    private final DefaultTableModel logModel=new DefaultTableModel(new Object[]{"Time","Event","Message"},0);
    private final JTextField nameField=new JTextField("New_photo.jpg",12);
    private final JComboBox<MediaType> typeBox=new JComboBox<>(MediaType.values());
    private final JSpinner sizeSpinner=new JSpinner(new SpinnerNumberModel(4.5,0.1,5000.0,0.5));
    private final JSpinner ratingSpinner=new JSpinner(new SpinnerNumberModel(4,1,5,1));
    private final JTextField dateField=new JTextField(LocalDate.now().toString(),8);
    private final JComboBox<String> sortBox=new JComboBox<>(new String[]{"Date","Rating","Title"});
    private final JLabel status=new JLabel("Ready");

    public GalleryApp() {
        super(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        gallery.subscribeObserver(new SwingEventObserver(logModel,e->status.setText(e[0]+":"+e[1])));
        gallery.subscribeObserver(new AuditLogger());

        JButton importBtn=new JButton("Import");
        importBtn.addActionListener(e->doImport());
        JButton demoBtn=new JButton("Load demo data");
        demoBtn.addActionListener(e->loadDemoData());
        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (Component c:new Component[]{new JLabel("File"),nameField,typeBox,new JLabel("MB"),sizeSpinner,
                new JLabel("Rating"),ratingSpinner,new JLabel("Date"),dateField,importBtn,demoBtn}) {
            top.add(c);
        }
        add(top, BorderLayout.NORTH);

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean sel,boolean foc) {
                Item it=items.get(v);
                String text=String.format("%s  |  %s  |  %s  |  rating %d  |  %.2f MB  |  filters: %s",
                        it.photo.getTitle(),it.media.getType(),it.photo.getDate(),it.photo.getRating(),
                        it.image.getFileSize(),it.filters.isEmpty()?"none":it.filters);
                return super.getListCellRendererComponent(l,text,i,sel,foc);
            }
        });
        add(new JScrollPane(list),BorderLayout.CENTER);

        JPanel side=new JPanel(new GridLayout(0,1,4,4));
        side.setBorder(BorderFactory.createTitledBorder("Actions"));
        side.add(button("Sepia",e -> applyFilter(FilterType.SEPIA)));
        side.add(button("Grayscale",e -> applyFilter(FilterType.GRAYSCALE)));
        side.add(button("Watermark",e -> applyFilter(FilterType.WATERMARK)));
        side.add(button("Export...",e -> export()));
        side.add(new JLabel("Sort by:"));
        side.add(sortBox);
        side.add(button("Sort",e -> doSort()));
        add(side, BorderLayout.EAST);

        JTable log=new JTable(logModel);
        JScrollPane logPane=new JScrollPane(log);
        logPane.setPreferredSize(new Dimension(0,150));
        JPanel bottom=new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createTitledBorder("Live event feed (Observer)"));
        bottom.add(logPane,BorderLayout.CENTER);
        bottom.add(status,BorderLayout.SOUTH);
        add(bottom,BorderLayout.SOUTH);
        setPreferredSize(new Dimension(1000,560));
    }

    private JButton button(String text,java.awt.event.ActionListener l) {
        JButton b=new JButton(text);
        b.addActionListener(l);
        return b;
    }

    void doImport() {
        try {
            importItem((MediaType) typeBox.getSelectedItem(), nameField.getText().trim(),
                    ((Number) sizeSpinner.getValue()).doubleValue(),
                    ((Number) ratingSpinner.getValue()).intValue(),LocalDate.parse(dateField.getText().trim()));
        } catch (RuntimeException ex) {
            error("Check the form (date format: 2026-11-11).");
        }
    }

    private void importItem(MediaType type,String name,double size,int rating,LocalDate date) {
        MediaFile media=gallery.importMedia(type,name,size);
        if (media==null) {
            error("Not enough storage for "+name);
            return;
        }
        Item it=new Item();
        it.media=media;
        it.photo=new Photo(name,date,rating);
        it.sizeMb=size;
        it.image=gallery.openImage(name,size);
        photos.add(it.photo);
        items.put(it.photo,it);
        refreshList(it.photo);
    }

    void loadDemoData() {
        LocalDate today=LocalDate.now();
        importItem(MediaType.PHOTO,"vacation_sunset.jpg",4.5,5,today.minusDays(120));
        importItem(MediaType.VIDEO,"family_birthday.mp4",60,4,today.minusDays(300));
        importItem(MediaType.PANORAMA,"mountains_360.jpg",12,5,today.minusDays(20));
        importItem(MediaType.PHOTO,"kitten.jpg",3.2,4,today.minusDays(5));
    }

    void doSort() {
        SortingStrategy s=sortBox.getSelectedIndex()==1?new SortByRatingStrategy()
                : sortBox.getSelectedIndex()==2?new SortByTitleStrategy():new SortByDateStrategy();
        Photo selected=list.getSelectedValue();
        gallery.sortPhotos(photos,s);
        refreshList(selected);
    }

    void applyFilter(FilterType type) {
        Item it=selected();
        if (it!=null) {
            it.image=gallery.applyFilter(it.image, type);
            it.filters+=(it.filters.isEmpty() ? "" : " + ")+type;
            list.repaint();
        }
    }

    private void export() {
        Item it = selected();
        if (it == null) {
            return;
        }
        JTextField res = new JTextField("1920x1080"), fmt = new JTextField("PNG"),
                quality = new JTextField("90"), dest = new JTextField("/gallery/exports");
        JCheckBox wm = new JCheckBox("Include watermark");
        Object[] form = {"Resolution", res, "Format (JPEG/PNG/WEBP/TIFF)", fmt, "Quality (1-100)", quality,
                "Destination", dest, wm};
        if (JOptionPane.showConfirmDialog(this, form, "Export settings (Builder)",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            ExportSettings s = new ExportSettingsBuilder().setResolution(res.getText().trim())
                    .setFormat(fmt.getText().trim()).setIncludeWatermark(wm.isSelected())
                    .setCompressionQuality(Integer.parseInt(quality.getText().trim()))
                    .setDestinationPath(dest.getText()).build();
            gallery.exportMedia(it.media, s);
        } catch (IllegalArgumentException ex) {
            error("Invalid export settings: " + ex.getMessage());
        }
    }

    private Item selected() {
        Photo p = list.getSelectedValue();
        if (p == null) {
            error("Select an item first.");
        }
        return p == null ? null : items.get(p);
    }

    private void refreshList(Photo select) {
        listModel.clear();
        photos.forEach(listModel::addElement);
        list.setSelectedValue(select, true);
    }

    private void error(String msg) {
        status.setText(msg);
        if (!GraphicsEnvironment.isHeadless()) {
            JOptionPane.showMessageDialog(this, msg, "Cannot do that", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Photo Gallery - Design Patterns Project");
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setContentPane(new GalleryApp());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
