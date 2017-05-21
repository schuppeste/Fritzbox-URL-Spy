package fritzscanner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class InteractiveTableModel extends AbstractTableModel {

    public static final int TITLE_INDEX = 0;
    public static final int ARTIST_INDEX = 1;
    public static final int ALBUM_INDEX = 2;
    public static final int HIDDEN_INDEX = 3;
    public static final int last_INDEX = 4;
    public static final int first_INDEX = 5;
    protected String[] columnNames;
    protected Vector dataVector;

    public InteractiveTableModel(String[] columnNames) {
        this.columnNames = columnNames;
        dataVector = new Vector();
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public boolean isCellEditable(int row, int column) {
        if (column == HIDDEN_INDEX) {
            return false;
        } else {
            return false;
        }
    }

    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return getValueAt(0, columnIndex).getClass();
        } else {
            return super.getColumnClass(columnIndex);
        }
    }

    public Object getValueAt(int row, int column) {
        NetRecord record = (NetRecord) dataVector.get(row);
        switch (column) {
            case TITLE_INDEX:
                return record.getDomain();
            case ARTIST_INDEX:
                return record.getSite();
            case ALBUM_INDEX:
                return record.getTime();

            default:
                return new Object();
        }
    }

    public void setValueAt(Object[] value, int row, int column) {

        try {
            NetRecord record = (NetRecord) dataVector.get(row);
            record.setDomain((String) value[0]);

            record.setSite((int) value[1]);

            //    record.setTime((int)value[2]);
            //  record.setTimel((int)value[3]);
        } catch (Exception e) {

            System.out.println("NEU");
            addEmptyRow();
            NetRecord record = (NetRecord) dataVector.get(row);
            record.setDomain((String) value[0]);

            record.setSite((int) value[1]);

            //record.setTime((int)value[2]);
            // record.setTimel((int)value[3]);

        }

        fireTableCellUpdated(row, column);
    }

    public boolean checkDoubles(Object[] value) {

        Comparator<NetRecord> comp = new NetRecord() {
            @Override
            public int compare(NetRecord t, NetRecord t1) {

                if (t.getSite() < t1.getSite()) {
                    return 1;
                }//To change body of generated methods, choose Tools | Templates.
                return -1;
            }
        };
        Collections.sort(dataVector, comp);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        for (int x = 0; x < dataVector.size(); x++) {
            NetRecord record = (NetRecord) dataVector.get(x);
            if (record.getDomain().equalsIgnoreCase((String) value[0])) {
                if (!record.getDomain().equals("example.de")) {
                    record.setSite(record.getSite() + 1);

                    record.setTime(dateFormat.format(date));


                }
                return false;
            }



        }
        return true;
    }

    public int getRowCount() {
        return dataVector.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public void Sorting() {
    }

    public boolean hasEmptyRow() {
        if (dataVector.size() == 0) {
            return false;
        }
        NetRecord netRecord = (NetRecord) dataVector.get(dataVector.size() - 1);
        if (netRecord.getDomain().trim().equals("")
                && netRecord.getSite() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addEmptyRow() {
        dataVector.add(new NetRecord() {
            @Override
            public int compare(NetRecord t, NetRecord t1) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        fireTableRowsInserted(
                dataVector.size() - 1,
                dataVector.size() - 1);
    }
}