package com.iamyasas.glossaryapp.view;

import com.iamyasas.glossaryapp.repository.DataSource;
import com.iamyasas.glossaryapp.repository.DataSourceRepository;
import com.iamyasas.glossaryapp.repository.Element;
import com.iamyasas.glossaryapp.repository.ElementRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
@SpringComponent
@UIScope
public class ElementEditor extends VerticalLayout implements KeyNotifier {

	private static final long serialVersionUID = -992288218498099864L;

	private final ElementRepository repository;

	/**
	 * The currently edited customer
	 */
	private Element element;

	/* Fields to edit properties in Element entity */
	IntegerField requirmentId = new IntegerField("Requirment Id");
	TextField name = new TextField("Name");
	TextField path = new TextField("Path");
	TextField description = new TextField("Description");
	TextField sampleValue = new TextField("Sample Value");
	TextField comments = new TextField("Comments");
	TextField apiName = new TextField("Api Name");
	

	BooleanComboBox isDraft = new BooleanComboBox("Is Draft");
	BooleanComboBox isApproved = new BooleanComboBox("Is Approved");
	BooleanComboBox isDeleted = new BooleanComboBox("Is Deleted");
	DatePicker createdDate = new DatePicker("Created Date");
	TextField createdBy = new TextField("Created By");
	DatePicker lastUpdatedDate = new DatePicker("Last Updated Date");
	TextField lastUpdatedBy = new TextField("Last Updated By");
	
	Label dataSourceLabel = new Label("Data Source");
	MultiSelectListBox<String> dataSourceNew = new MultiSelectListBox<>();
	
	HorizontalLayout fields1 = new HorizontalLayout(requirmentId, name, path, description, sampleValue, comments, apiName);
	HorizontalLayout fields2 = new HorizontalLayout(isDraft, isApproved, isDeleted, createdDate, createdBy, lastUpdatedDate, lastUpdatedBy);

	/* Action buttons */
	// TODO why more code?
	Button save = new Button("Save", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<Element> binder = new Binder<>(Element.class);
	private ChangeHandler changeHandler;

	@Autowired
	public ElementEditor(ElementRepository repository, DataSourceRepository sourceRepository) {
		this.repository = repository;
		
		//dataSource.setItems("source 1", "source 2", "source 3", "source 1", "source 2", "source 3");
		dataSourceNew.setHeight("100px");
		//dataSourceNew.setItems(sourceRepository.findAll().stream().map((dataSource) -> dataSource.getSource()));
		DataProvider<String, Void> dataProvider = DataProvider.fromCallbacks(
			query -> sourceRepository.findAll().stream().map(DataSource::getSource), 
			Query -> (int) sourceRepository.count()
		);
				
		//sourceRepository.findAll().stream().map((dataSource) -> dataSource.getSource()).collect(Collectors.toList());
		dataSourceNew.setDataProvider(dataProvider);

		add(fields1, fields2, dataSourceLabel, dataSourceNew, actions);

		binder.bind(dataSourceNew, 
				element1 -> element1.getDataSources().stream().map(ds -> ds.getSource()).collect(Collectors.toSet()), 
				(element2, dataSources1) -> element2.setDataSources(dataSources1.stream().map(DataSource::new).collect(Collectors.toSet())));
		
		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);

		save.getElement().getThemeList().add("primary");
		delete.getElement().getThemeList().add("error");

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> editCustomer(element));
		setVisible(false);
	}

	void delete() {
		repository.delete(element);
		changeHandler.onChange();
		dataSourceNew.getDataProvider().refreshAll();
	}

	void save() {
		repository.save(element);
		changeHandler.onChange();
		dataSourceNew.getDataProvider().refreshAll();
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editCustomer(Element c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			element = repository.findById(c.getId()).get();
		}
		else {
			element = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(element);

		setVisible(true);

		// Focus first name initially
		requirmentId.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

}
