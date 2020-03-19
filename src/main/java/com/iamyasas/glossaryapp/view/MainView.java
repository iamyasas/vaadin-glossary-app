package com.iamyasas.glossaryapp.view;

import com.iamyasas.glossaryapp.repository.Element;
import com.iamyasas.glossaryapp.repository.ElementRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = -8428515995289165732L;

	private final ElementRepository repo;

	final Grid<Element> grid;

	final TextField filter;

	private final Button addNewBtn;

	public MainView(ElementRepository repo, ElementEditor editor) {
		this.repo = repo;
		this.grid = new Grid<>(Element.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New element", VaadinIcon.PLUS.create());

		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		add(actions, grid, editor);

		grid.setHeight("250px");
		grid.setColumns("id", "requirmentId", "name", "path", "description","apiName","isDraft","isApproved","isDeleted");
		//grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		filter.setPlaceholder("Filter by name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.EAGER);
		filter.addValueChangeListener(e -> listElements(e.getValue()));

		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editCustomer(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editCustomer(new Element("", "", "", null)));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listElements(filter.getValue());
		});

		// Initialize listing
		listElements(null);
	}

	// tag::listCustomers[]
	void listElements(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.findAll());
		}
		else {
			grid.setItems(repo.findByNameStartsWithIgnoreCase(filterText));
		}
	}
	// end::listCustomers[]

}
