package view;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


class EventTableRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7668769391878417516L;

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
		   if (column !=4 ) 
		   {
			   super.setBackground(Color.WHITE);
		    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		    return this;
		   }

		    if (value.equals("true")) {
		     super.setBackground(Color.RED);
		    } else {
		     super.setBackground(Color.GREEN);
		    }
		    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		   return this;
		}
	}
