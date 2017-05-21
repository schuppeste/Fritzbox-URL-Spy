/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fritzscanner;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
 
public class Renderer extends DefaultTableCellRenderer{
   @Override
  public void setValue( Object value )
  {
    if ( value instanceof Long )
    {
      setForeground( (Long) value % 2 == 0 ? Color.BLUE : Color.GRAY );

      setText( value.toString() );
    }
    else
      super.setValue( value );
  }

}