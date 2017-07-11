package fi.service;

import fi.controller.ComboController;
import fi.dao.ItemDAO;
import fi.model.Category;
import fi.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    private static final int ITEM_EXEC_TIME = 7000;
    private static List<String> failReasons = new ArrayList<>();

    @Autowired
    ItemDAO itemDAO;

    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }

    public Map<Item, List<Item>> getAllItemDependencies() {
        return itemDAO.getAllItemDependencies();
    }

    public void addItemDependency(final int depender, final int dependee) {
        itemDAO.createItemDependency(depender, dependee);
    }

    public List<Category> getAllCategories() {
        return itemDAO.getAllCategories();
    }

    public int createCategory(final String name) {
        return itemDAO.createCategory(name);
    }

    public int createItem(final String name, final int categoryId) {
        return itemDAO.createItem(name, categoryId);
    }

    public Map<String, Object> executeCombo(final List<Integer> executeItemIds, final ComboController comboController, final String guid) throws InterruptedException {
        final Map<String, Object> resultMap = new HashMap<>();

        final LinkedHashSet<Item> executeItems = itemDAO.getExecuteItemsByIds(executeItemIds);
        if (!isComboValid(executeItems)) {
            resultMap.put("success", false);

            String failure = "";
            for (final String failReason : failReasons) {
                failure += failReason;
            }
            resultMap.put("failReason", failure);
            return resultMap;
        }

        // Execute Items in order
        final Iterator<Integer> itemIdIterator = executeItemIds.iterator();
        final long startTime = System.currentTimeMillis();
        String executionResult = "";
        while (itemIdIterator.hasNext()) {
            final Item executeItem = itemDAO.getItemById(itemIdIterator.next());
            executionResult += executeItem.getName() + " + ";
            final String status = "Running " + executeItem;
            logger.info(status);
            Thread.sleep(ITEM_EXEC_TIME / 2);
            comboController.sendStatus(status, guid, !itemIdIterator.hasNext());
            Thread.sleep(ITEM_EXEC_TIME / 2);
        }
        executionResult = executionResult.substring(0, executionResult.length() - 3);
        final long msElapsed = System.currentTimeMillis() - startTime;

        logger.info("Result: " + executionResult);
        logger.info("Time elapsed (ms): " + msElapsed);

        resultMap.put("success", true);
        resultMap.put("result", "Your pair of shoes was built: " + executionResult);
        resultMap.put("msElapsed", msElapsed);
        return resultMap;
    }


    // Validate combo
    private boolean isComboValid(final LinkedHashSet<Item> items) {
        failReasons.clear();
        // Traverse through ordered list of selected items
        final Set<Item> passedItems = new HashSet<>();
        final Iterator<Item> itemsIterator = items.iterator();
        while (itemsIterator.hasNext()) {
            final Item item = itemsIterator.next();
            passedItems.add(item);

            // Check item dependencies
            final Iterator<Item> dependenciesIterator = item.getItemDependencies().iterator();
            while (dependenciesIterator.hasNext()) {
                final Item dependeeItem = dependenciesIterator.next();
                logger.debug("Dependency: " + item + " depends on " + dependeeItem);
                if (!passedItems.contains(dependeeItem)) {
                    failReasons.add("Dependency not satisfied: " + item + " requires " + dependeeItem + "\n");
                    logger.error("Item dependency error: " + item + " requires " + dependeeItem);
                }
            }
        }
        return failReasons.size() == 0;
    }


}
