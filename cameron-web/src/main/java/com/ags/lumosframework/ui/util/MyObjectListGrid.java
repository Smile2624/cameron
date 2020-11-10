package com.ags.lumosframework.ui.util;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.base.domain.IDataImpl;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.filter.dql.Order;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IDomainObjectGrid;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IObjectClickListener;
import com.ags.lumosframework.web.vaadin.component.paginationobjectlist.IObjectSelectionListener;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.*;
import com.vaadin.ui.renderers.AbstractRenderer;
import com.vaadin.ui.renderers.TextRenderer;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class MyObjectListGrid<T extends ObjectBaseImpl<?>> extends BaseView implements IDomainObjectGrid<T> {
    private static final long serialVersionUID = -3903777668282783935L;
    public static int LOADING_DATA_SIZE = 50;
    public Set<String> columnNameList;
    protected Grid<T> grid;
    private Class<?> serviceClass;
    private IBaseDomainObjectService<?> service;
    private IObjectClickListener<T> objectClickListener;
    private IObjectSelectionListener<T> objectSelectionListener;
    private List<T> data;
    private boolean isLocalModel;
    private boolean isDialog;
    private String serviceBeanName;
    private EntityFilter entityFilter;

    public MyObjectListGrid() {
        this(true);
    }

    public MyObjectListGrid(boolean hasName) {
        this.columnNameList = new HashSet();
        this.grid = new Grid();
        this.serviceClass = null;
        this.objectClickListener = null;
        this.objectSelectionListener = null;
        this.data = null;
        this.isLocalModel = false;
        this.isDialog = false;
        this.entityFilter = null;
        Grid.Column descriptionColumn;
        if (hasName) {
            descriptionColumn = this.grid.addColumn(IDataImpl::getName);
            descriptionColumn.setCaption(I18NUtility.getValue("Common.Name", "Name", new Object[0]));


            descriptionColumn = this.addDescriptionColumn();
            descriptionColumn.setCaption(I18NUtility.getValue("Common.Description", "Description", new Object[0]));
            descriptionColumn.setSortOrderProvider(new SortOrderProvider() {
                private static final long serialVersionUID = 1L;

                @Override
                public Stream<QuerySortOrder> apply(SortDirection sortDirection) {
                    List<QuerySortOrder> list = new ArrayList();
                    list.add(new QuerySortOrder("description", sortDirection));
                    return list.stream();
                }
            });
        }
        this.grid.addItemClickListener((event) -> {
            if (this.objectClickListener != null) {
                this.objectClickListener.itemClicked(event);
            }

        });
        this.grid.addSelectionListener(new SelectionListener<T>() {
            private static final long serialVersionUID = 4404475737160537026L;

            @Override
            public void selectionChange(SelectionEvent<T> event) {
                if (MyObjectListGrid.this.objectSelectionListener != null) {
                    MyObjectListGrid.this.objectSelectionListener.itemClicked(event);
                }

            }
        });
        this.grid.setSizeFull();
        this.setSizeFull();
        this.setCompositionRoot(this.grid);
    }

    protected Grid.Column<T, String> addDescriptionColumn() {
        return this.grid.addColumn(ObjectBaseImpl::getDescription);
    }

    @Override
    public void setServiceClass(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
        this.isLocalModel = false;
    }

    @Override
    public void setServiceClass(Class<?> serviceClass, String serviceBeanName) {
        this.serviceClass = serviceClass;
        this.serviceBeanName = serviceBeanName;
        this.isLocalModel = false;
    }

    @Override
    public void setData(List<T> data) {
        this.data = data;
        this.isLocalModel = true;
    }

    @Override
    public void refresh() {
        if (this.isLocalModel) {
            ListDataProvider<T> ofCollection = DataProvider.ofCollection(this.data);
            this.grid.setDataProvider(ofCollection);
            if (!this.data.isEmpty()) {
                this.grid.select(this.data.get(0));
            }
        } else {
            DataProvider<T, Void> provider = DataProvider.fromCallbacks((query) -> {
                List<QuerySortOrder> list = query.getSortOrders();
                EntityFilter filter = this.getEntityFilter();
                filter = this.initServiceAndFilter(filter);
                Iterator var4 = list.iterator();

                while (var4.hasNext()) {
                    QuerySortOrder sort = (QuerySortOrder) var4.next();
                    if (sort.getDirection().equals(SortDirection.ASCENDING)) {
                        filter.addOrder(Order.asc(sort.getSorted(), false));
                    } else {
                        filter.addOrder(Order.desc(sort.getSorted(), false));
                    }
                }

                filter.setStartPosition(query.getOffset());
                filter.setMaxResult(LOADING_DATA_SIZE);
                List<T> persons = (List<T>) this.service.listByFilter(filter);
                return persons.stream();
            }, (query) -> {
                EntityFilter filter = this.getEntityFilter();
                filter = this.initServiceAndFilter(filter);
                return this.service.countByFilter(filter);
            });
            this.grid.setDataProvider(provider);
        }

    }

    public EntityFilter initServiceAndFilter(EntityFilter filter) {
        if (this.service == null) {
            if (this.serviceBeanName != null) {
                this.service = (IBaseDomainObjectService) BeanManager.getService(this.serviceBeanName, this.serviceClass);
            } else {
                this.service = (IBaseDomainObjectService) BeanManager.getService(this.serviceClass);
            }
        }

        if (filter == null) {
            filter = this.service.createFilter();
        }

        return filter;
    }

    @Override
    public void refresh(T item) {
        this.grid.getDataProvider().refreshItem(item);
    }

    @Override
    public void setObjectClickListener(IObjectClickListener<T> listener) {
        this.objectClickListener = listener;
    }

    @Override
    public void setObjectSelectionListener(IObjectSelectionListener<T> listener) {
        this.objectSelectionListener = listener;
    }

    @Override
    public T getSelectedObject() {
        Iterator<T> iterator = this.grid.getSelectedItems().iterator();
        return iterator.hasNext() ? (T) iterator.next() : null;
    }

    public EntityFilter getEntityFilter() {
        if (this.entityFilter == null) {
        }

        return this.entityFilter;
    }

    @Override
    public void setFilter(Object filter) {
        this.entityFilter = (EntityFilter) filter;
    }

    @Override
    public Grid.SelectionMode getSelectionMode() {
        GridSelectionModel<T> selectionModel = this.grid.getSelectionModel();
        Grid.SelectionMode mode = null;
        if (selectionModel.getClass().equals(SingleSelectionModelImpl.class)) {
            mode = Grid.SelectionMode.SINGLE;
        } else if (selectionModel.getClass().equals(MultiSelectionModelImpl.class)) {
            mode = Grid.SelectionMode.MULTI;
        } else if (selectionModel.getClass().equals(NoSelectionModel.class)) {
            mode = Grid.SelectionMode.NONE;
        }

        return mode;
    }

    @Override
    public void setSelectionMode(Grid.SelectionMode selectionMode) {
        this.grid.setSelectionMode(selectionMode);
    }

    @Override
    public List<T> getSelections() {
        List<T> selections = new ArrayList();
        selections.addAll(this.grid.getSelectedItems());
        return selections;
    }

    @Override
    public void select(List<T> selected) {
        this.grid.deselectAll();
        Grid var10001 = this.grid;
        selected.forEach(var10001::select);
    }

    @Override
    public void setSort(String columnName) {
        this.columnNameList.add(columnName);
    }

    @Override
    public void clearSortColumnName() {
        this.columnNameList.clear();
    }

    @Override
    public void setIsDialog(boolean isDialog) {
        this.isDialog = isDialog;
    }

    @Override
    public Grid.Column<T, ?> addColumn(String propertyName) {
        return this.grid.addColumn(propertyName, new TextRenderer()).setSortOrderProvider((direction) -> {
            List<QuerySortOrder> list = new ArrayList();
            list.add(new QuerySortOrder(propertyName, direction));
            return list.stream();
        }).setHidable(true);
    }

    @Override
    public Grid.Column<T, ?> addColumn(String propertyName, AbstractRenderer<? super T, ?> renderer) {
        return this.grid.addColumn(propertyName, renderer).setSortOrderProvider((direction) -> {
            List<QuerySortOrder> list = new ArrayList();
            list.add(new QuerySortOrder(propertyName, direction));
            return list.stream();
        }).setHidable(true);
    }

    @Override
    public <V> Grid.Column<T, V> addColumn(ValueProvider<T, V> valueProvider) {
        return this.grid.addColumn(valueProvider, new TextRenderer()).setHidable(true);
    }

    @Override
    public <V> Grid.Column<T, V> addColumn(ValueProvider<T, V> valueProvider, AbstractRenderer<? super T, ? super V> renderer) {
        return this.grid.addColumn(valueProvider, renderer).setHidable(true);
    }

    @Override
    public <V> Grid.Column<T, V> addColumn(ValueProvider<T, V> valueProvider, ValueProvider<V, String> presentationProvider) {
        return this.grid.addColumn(valueProvider, presentationProvider).setHidable(true);
    }

    @Override
    public <V, P> Grid.Column<T, V> addColumn(ValueProvider<T, V> valueProvider, ValueProvider<V, P> presentationProvider, AbstractRenderer<? super T, ? super P> renderer) {
        return this.grid.addColumn(valueProvider, presentationProvider, renderer).setHidable(true);
    }

    @Override
    public <V extends Component> Grid.Column<T, V> addComponentColumn(ValueProvider<T, V> componentProvider) {
        return this.grid.addComponentColumn(componentProvider).setHidable(true);
    }

    @Override
    public void attachFixedColumns(boolean hasNameColumn) {
        this.grid.addColumn(ObjectBaseImpl::getCreateUserName).setHidable(true).setSortable(true).setCaption(I18NUtility.getValue("common.creationuser", "Creation User", new Object[0])).setDescriptionGenerator(ObjectBaseImpl::getCreateUserName);
        this.grid.addColumn((buildtimeObjectBaseImpl) -> {
            return buildtimeObjectBaseImpl.getCreateTime() == null ? "" : buildtimeObjectBaseImpl.getCreateTime().withZoneSameInstant(RequestInfo.current().getUserZoneId()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }).setHidable(true).setSortable(true).setCaption(I18NUtility.getValue("common.creationtime", "Creation Time", new Object[0])).setHidden(hasNameColumn);
        this.grid.addColumn(ObjectBaseImpl::getLastModifyUserName).setHidable(true).setSortable(true).setCaption(I18NUtility.getValue("common.lastmodifieduser", "Last Modified User", new Object[0]));
        this.grid.addColumn((buildtimeObjectBaseImpl) -> {
            return buildtimeObjectBaseImpl.getLastModifyTime() == null ? "" : buildtimeObjectBaseImpl.getLastModifyTime().withZoneSameInstant(RequestInfo.current().getUserZoneId()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }).setHidable(true).setSortable(true).setCaption(I18NUtility.getValue("common.lastmodifiedtime", "Last Modified Time", new Object[0]));
    }

    @Override
    public void setStartPage(int currentPage, int startPosition) {
        throw new PlatformException("not supported, please call PaginationObjectListGrid instead");
    }

    @Override
    public void setPageSize(int pageSize) {
        throw new PlatformException("not supported, please call PaginationObjectListGrid instead");
    }

    @Override
    public void addStyleNameToGrid(String style) {
        this.grid.addStyleName(style);
    }
}
