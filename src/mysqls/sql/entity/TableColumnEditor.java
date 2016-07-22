/***实体关系图和sql生产的实现*/
package mysqls.sql.entity;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/***
 *
 * @author 长宏
 **/
public class TableColumnEditor extends PropertyEditorSupport {

	private TableColumn mcolumn;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.beans.PropertyEditorSupport#getCustomEditor()
	 */
	@Override
	public Component getCustomEditor() {
		// TODO Auto-generated method stub
		TableColumn column = (TableColumn) getValue();
		String name = column.getName();
		String type = column.getType();
		String defaul = column.getDefaultvalues();
		boolean notnull = column.isNotnull();
		boolean pri = column.isPrimarykey();
		boolean unique = column.isUnique();
		boolean forigrn = column.isForeignKey();
		super.getCustomEditor();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 0));

		// try {
		// PropertyDescriptor[] descriptors =
		// Introspector.getBeanInfo(pBean.getClass()).getPropertyDescriptors()
		// .clone();
		//
		// for (PropertyDescriptor descriptor : descriptors) {
		// PropertyEditor editor = getEditor(pBean, descriptor);
		// String propertyName =
		// PropertySheets.getPropertyName(pBean.getClass(),
		// descriptor.getName());
		// if (editor != null &&
		// !propertyName.equals(PropertySheets.INVISIBLE_PROPERTY_MARKER)) {
		// add(new JLabel(propertyName));
		// add(getEditorComponent(editor));
		// }
		// }}catch(
		//
		// IntrospectionException exception)
		// {
		// // Do nothing
		// }
		//
		return panel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.beans.PropertyEditorSupport#supportsCustomEditor()
	 */
	@Override
	public boolean supportsCustomEditor() {
		// TODO Auto-generated method stub
		return true;
	}

	public PropertyEditor getEditor(PropertyDescriptor pDescriptor) {
		try {
			final Method getter = pDescriptor.getReadMethod();
			final Method setter = pDescriptor.getWriteMethod();
			if (getter == null || setter == null) {
				return null;
			}

			Class<?> type = pDescriptor.getPropertyType();
			final PropertyEditor editor;
			Class<?> editorClass = pDescriptor.getPropertyEditorClass();
			if (editorClass != null) {
				editor = (PropertyEditor) editorClass.newInstance();
			} else {
				editor = PropertyEditorManager.findEditor(type);
			}
			if (editor == null) {
				return null;
			}
			return editor;
		} catch (InstantiationException | IllegalAccessException exception) {
			return null;
		}
	}

	public Component getEditorComponent(final PropertyEditor pEditor) {
		String[] tags = pEditor.getTags();
		String text = pEditor.getAsText();
		if (pEditor.supportsCustomEditor()) {
			return pEditor.getCustomEditor();

		} else if (tags != null) {
			// make a combo box that shows all tags
			final JComboBox<String> comboBox = new JComboBox<>(tags);
			comboBox.setSelectedItem(text);
			comboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent pEvent) {
					if (pEvent.getStateChange() == ItemEvent.SELECTED) {
						pEditor.setAsText((String) comboBox.getSelectedItem());
					}
				}
			});
			return comboBox;
		} else {
			final JTextField textField = new JTextField(text, 10);
			textField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void insertUpdate(DocumentEvent pEvent) {
					pEditor.setAsText(textField.getText());
				}

				@Override
				public void removeUpdate(DocumentEvent pEvent) {
					pEditor.setAsText(textField.getText());
				}

				@Override
				public void changedUpdate(DocumentEvent pEvent) {
				}

			});
			return textField;
		}
	}

}