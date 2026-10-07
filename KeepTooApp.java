
package keeptooapp;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class KeepTooApp extends JFrame {

    // Colours taken from the screenshot
    private static final Color SIDEBAR      = new Color(60, 35, 100);
    private static final Color SIDEBAR_SEL  = new Color(95, 70, 135);
    private static final Color SIDEBAR_HOVER= new Color(78, 52, 118);
    private static final Color PURPLE       = new Color(124, 71, 222);
    private static final Color LIGHT_GREY   = new Color(240, 240, 240);

    private final JPanel contentCards = new JPanel(new CardLayout());
    private final JLabel breadcrumb = new JLabel("Adminstrations/Funding/");
    private final JTextField nameField = new JTextField(18);
    private final JComboBox<String> monthBox = new JComboBox<>(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"});
    private final JComboBox<String> yearBox  = new JComboBox<>(new String[]{"2015","2016","2017","2018","2019"});
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"DATE", "REF .NO", "PARTICULARS"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private TableRowSorter<DefaultTableModel> sorter;
    private final java.util.List<JButton> menuButtons = new java.util.ArrayList<>();

    private final String[][] allData = {
        {"5/7/2017", "82727", "Locks"},
        {"8/8/2017", "27286", ""},
        {"2/7/2017", "08991", "Criuke"}
    };

    public KeepTooApp() {
        setTitle("KeepToo");
        setUndecorated(true);                 // so we can use our own "X"
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildMain(), BorderLayout.CENTER);

        loadTable();
        selectMenu(0);
    }

    // ---------------- LEFT SIDEBAR ----------------
    private JPanel buildSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setBackground(SIDEBAR);
        side.setPreferredSize(new Dimension(250, 0));

        // Logo area
        JPanel logo = new JPanel(new BorderLayout());
        logo.setBackground(SIDEBAR);
        logo.setBorder(BorderFactory.createEmptyBorder(35, 40, 20, 40));
        JLabel title = new JLabel("KeepToo");
        title.setForeground(new Color(220, 210, 235));
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JSeparator line = new JSeparator();
        line.setForeground(Color.WHITE);
        logo.add(title, BorderLayout.CENTER);
        logo.add(line, BorderLayout.SOUTH);
        logo.setPreferredSize(new Dimension(250, 110));

        // Menu items
        JPanel menu = new JPanel(new GridLayout(5, 1));
        menu.setBackground(SIDEBAR);
        String[] items = {"Employee Benefits", "Gifts & incentives", "Coming soon", "Administration", "Document Store"};
        String[] crumbs = {"Employee Benefits/", "Gifts & incentives/", "Coming soon/", "Adminstrations/Funding/", "Document Store/"};

        for (int i = 0; i < items.length; i++) {
            final int index = i;
            JButton b = new JButton("   " + items[i]);
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setForeground(new Color(230, 225, 240));
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setBackground(SIDEBAR);
            b.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
            b.setFocusPainted(false);
            b.setContentAreaFilled(false);
            b.setOpaque(true);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    if (b.getBackground() != SIDEBAR_SEL) b.setBackground(SIDEBAR_HOVER);
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (b.getBackground() != SIDEBAR_SEL) b.setBackground(SIDEBAR);
                }
            });
            b.addActionListener(e -> {
                selectMenu(index);
                breadcrumb.setText(crumbs[index]);
            });
            menuButtons.add(b);
            menu.add(b);
        }
        JPanel menuWrap = new JPanel(new BorderLayout());
        menuWrap.setBackground(SIDEBAR);
        menuWrap.add(menu, BorderLayout.NORTH);
        menu.setPreferredSize(new Dimension(250, 235));

        side.add(logo, BorderLayout.NORTH);
        side.add(menuWrap, BorderLayout.CENTER);
        return side;
    }

    private void selectMenu(int index) {
        for (int i = 0; i < menuButtons.size(); i++) {
            menuButtons.get(i).setBackground(i == index ? SIDEBAR_SEL : SIDEBAR);
        }
        ((CardLayout) contentCards.getLayout()).show(contentCards, index == 0 || index == 3 ? "history" : "other" + index);
    }

    // ---------------- RIGHT SIDE ----------------
    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        main.add(buildTopBar(), BorderLayout.NORTH);

        contentCards.add(buildHistoryPanel(), "history");
        for (int i = 1; i <= 4; i++) {
            if (i == 3) continue; // index 3 uses the history panel
            JPanel p = new JPanel(new GridBagLayout());
            p.setBackground(Color.WHITE);
            p.add(new JLabel("Page under construction"));
            contentCards.add(p, "other" + i);
        }
        main.add(contentCards, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 12));
        top.setPreferredSize(new Dimension(0, 55));

        JLabel love = new JLabel("We \u2665 KeepToo");
        love.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel close = new JLabel("X");
        close.setFont(new Font("Segoe UI", Font.BOLD, 20));
        close.setForeground(SIDEBAR);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { System.exit(0); }
        });

        top.add(love, BorderLayout.WEST);
        top.add(close, BorderLayout.EAST);
        return top;
    }

    private JPanel buildHistoryPanel() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(Color.WHITE);

        // Purple header
        JPanel header = new JPanel(null);
        header.setBackground(PURPLE);
        header.setPreferredSize(new Dimension(0, 150));

        breadcrumb.setForeground(new Color(235, 225, 250));
        breadcrumb.setFont(new Font("Segoe UI", Font.BOLD, 12));
        breadcrumb.setBounds(60, 35, 400, 20);

        JLabel heading = new JLabel("Transaction History for");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        heading.setBounds(60, 70, 250, 40);

        nameField.setOpaque(false);
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        nameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        nameField.setBounds(300, 75, 320, 32);

        header.add(breadcrumb);
        header.add(heading);
        header.add(nameField);

        // Body: filters + table
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 5));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filters.setBackground(Color.WHITE);
        monthBox.setPreferredSize(new Dimension(170, 22));
        yearBox.setPreferredSize(new Dimension(170, 22));
        monthBox.setSelectedItem("4");
        yearBox.setSelectedItem("2017");
        filters.add(monthBox);
        filters.add(Box.createHorizontalStrut(65));
        filters.add(yearBox);
        filters.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Table styling
        table.setRowHeight(24);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PURPLE);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(LIGHT_GREY);

        // Functionality: filter when month/year change, show selected row on double-click
        ActionListener filterListener = e -> applyFilter();
        monthBox.addActionListener(filterListener);
        yearBox.addActionListener(filterListener);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    int r = table.convertRowIndexToModel(table.getSelectedRow());
                    JOptionPane.showMessageDialog(KeepTooApp.this,
                        "Date: " + model.getValueAt(r, 0) + "\nRef no: " + model.getValueAt(r, 1)
                        + "\nParticulars: " + model.getValueAt(r, 2),
                        "Transaction", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        body.add(filters, BorderLayout.NORTH);
        body.add(scroll, BorderLayout.CENTER);

        page.add(header, BorderLayout.NORTH);
        page.add(body, BorderLayout.CENTER);
        return page;
    }

    private void loadTable() {
        for (String[] row : allData) model.addRow(row);
    }

    /** Shows only rows whose date is in the chosen month and year (format d/M/yyyy). */
    private void applyFilter() {
        String m = (String) monthBox.getSelectedItem();
        String y = (String) yearBox.getSelectedItem();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> en) {
                String[] parts = en.getStringValue(0).split("/");
                return parts.length == 3 && parts[1].equals(m) && parts[2].equals(y);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KeepTooApp().setVisible(true));
    }
}
    
    

