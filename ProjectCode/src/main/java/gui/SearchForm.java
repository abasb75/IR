package gui;

import javax.swing.*;
import java.awt.*;

public class SearchForm extends JFrame {
    JPanel searchInputPanel,searchInput;
    public JButton doSearch;
    public JTextField getSearchQuery;
    public JList listView;
    public DefaultListModel<String> model;
    public SearchForm(){
        this.setLayout(null);
        this.setSize(980,650);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(250,250,250));
        createSearchInput();
        createBottom();
        createList();
    }
    private void createList() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBounds(0,55,980,520);
        panel.setBackground(new Color(0,0,0));
        this.add(panel);
        model = new DefaultListModel<>();
        listView = new JList(model);

        listView.setFont(FontManager.createFont("Roboto-Black.ttf",14f));

        panel.setAutoscrolls(true);
        ListCellRenderer renderer = new cellRender();
        listView.setCellRenderer(renderer);
        listView.setAutoscrolls(true);

        JScrollPane scroll = new JScrollPane(listView);
        panel.add(scroll);
        scroll.setPreferredSize(new Dimension(980,518));
        scroll.setLocation(0,0);

    }
    private void createBottom() {
        JPanel bottom = new JPanel();
        bottom.setLayout(null);
        this.add(bottom);
        bottom.setBounds(0,573,980,40);
        bottom.setBackground(new Color(220,220,220));

        JLabel abasbagheri = new JLabel("عباس باقری",SwingConstants.CENTER);
        abasbagheri.setFont(FontManager.createFont("BNaznnBd.ttf",14f));
        abasbagheri.setForeground(new Color(127,127,127));
        abasbagheri.setBounds(440,0,100,40);
        bottom.add(abasbagheri);
    }
    private void createSearchInput() {
        searchInputPanel = new JPanel();
        this.add(searchInputPanel);
        searchInputPanel.setBackground(new Color(235,235,235));
        searchInputPanel.setBounds(0,0,980,60);
        searchInputPanel.setLayout(null);

        searchInput = new JPanel();
        searchInputPanel.add(searchInput);
        searchInput.setBounds(140,5,700,50);
        searchInput.setBackground(new Color(248,248,248));
        searchInput.setLayout(null);

        doSearch = new JButton("جستجو");
        doSearch.setHorizontalAlignment(SwingConstants.CENTER);
        doSearch.setVerticalAlignment(SwingConstants.CENTER);
        searchInput.add(doSearch);
        doSearch.setBounds(0,0,100,50);
        doSearch.setBackground(new Color(225,225,225));
        doSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        doSearch.setBorder(BorderFactory.createEmptyBorder());
        doSearch.setFont(FontManager.createFont("BNaznnBd.ttf",14f));
        doSearch.setForeground(new Color(127,127,127));

        getSearchQuery = new JTextField();
        searchInput.add(getSearchQuery);
        getSearchQuery.setBounds(110,0,580,50);
        getSearchQuery.setBorder(BorderFactory.createEmptyBorder());
        getSearchQuery.setMargin(new Insets(0,10,0,10));
        getSearchQuery.setBackground(new Color(248,248,248));



    }

    public static class cellRender implements ListCellRenderer {
        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
            renderer.setPreferredSize(new Dimension(960, 40));
            if (index % 2 == 0) {
                renderer.setBackground(new Color(250, 250, 250));
            } else {
                renderer.setBackground(new Color(245, 245, 245));
            }
            renderer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            //renderer.setFont(FontManager.createFont("Ubuntu-R.ttf",14f));
            return renderer;
        }

    }

}
