package fi.controller;

import fi.model.Category;
import fi.model.Item;
import fi.service.ItemService;
import fi.wrapper.ExecutionStatusWrapper;
import fi.wrapper.ItemDependencyWrapper;
import fi.wrapper.ExecutionInfoWrapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Transactional
public class ComboController {

    private static final Logger logger = LoggerFactory.getLogger(ComboController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Retrieve items
    @RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> getItems() {
        final Iterator<Item> iterator = itemService.getAllItems().iterator();
        final List<Map<String, Object>> resultList = new ArrayList<>();
        while (iterator.hasNext()) {
            final Map<String, Object> resultMap = new LinkedHashMap<>();
            final Item item = iterator.next();
            resultMap.put("item_id", item.getId());
            resultMap.put("name", item.getName());
            resultMap.put("category_id", item.getCategory().getId());
            //resultMap.put("ordering", item.getCategory().getOrdering());
            resultList.add(resultMap);
        }
        return resultList;
    }

    // Retrieve item dependencies
    @RequestMapping(value = "/itemdependencies", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getItemDependencies() {
        final JSONArray baseArray = new JSONArray();
        final Map<Item, List<Item>> itemDependencies = itemService.getAllItemDependencies();

        for (final Item item : itemDependencies.keySet()) {
            final JSONObject dependency = new JSONObject();

            final JSONArray dependeesArray = new JSONArray();
            for (final Item dependee : itemDependencies.get(item)) {
                dependeesArray.put(dependee.getName());
            }
            dependency.put("depender", item.getName());
            dependency.put("dependees", dependeesArray);
            baseArray.put(dependency);
        }

        logger.info(baseArray.toString());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(baseArray.toString(), HttpStatus.OK);
    }

    // Add item dependency
    @RequestMapping(value = "/itemdependencies", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity addItemDependency(@RequestBody ItemDependencyWrapper itemDependencyWrapper) {
        itemService.addItemDependency(itemDependencyWrapper.getDepender(), itemDependencyWrapper.getDependee());
        return new ResponseEntity(HttpStatus.OK);
    }

    // Retrieve categories
    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = "application/json")
    public List<Map<String, Object>> getCategories() {
        final Iterator<Category> results = itemService.getAllCategories().iterator();
        final List<Map<String, Object>> resultList = new ArrayList<>();
        while (results.hasNext()) {
            final Map<String, Object> resultMap = new LinkedHashMap<>();
            final Category category = results.next();
            resultMap.put("id", category.getId());
            resultMap.put("name", category.getName());
            //resultMap.put("ordering", category.getOrdering());
            resultList.add(resultMap);
        }
        return resultList;
    }

    // Create item
    @PostMapping(value = "/items", consumes = "application/json", produces = "application/json")
    public ResponseEntity createItem(@RequestBody String json) {
        final JSONObject jsonObject = new JSONObject(json);
        final int itemId = itemService.createItem(jsonObject.getString("name"), jsonObject.getInt("categoryId"));
        //final int itemId = itemService.createItem(item);
        return new ResponseEntity(Collections.singletonMap("item_id", itemId), HttpStatus.OK);
    }

    // Create category
    @PostMapping(value = "/categories", consumes = "application/json", produces = "application/json")
    public ResponseEntity createCategory(@RequestBody String json) {
        final int categoryId = itemService.createCategory(new JSONObject(json).getString("name"));
        return new ResponseEntity(Collections.singletonMap("category_id", categoryId), HttpStatus.OK);
    }

    // Execute combo and return result
    @PostMapping(value = "/execute", consumes = "application/json", produces = "application/json")
    public ResponseEntity executeCombo(@RequestBody ExecutionInfoWrapper executionInfoWrapper) throws InterruptedException {
        final Map<String, Object> resultMap = itemService.executeCombo(executionInfoWrapper.getItems(), this, executionInfoWrapper.getGuid());
        if (resultMap.get("success").equals(true)) {
            return new ResponseEntity(resultMap, HttpStatus.OK);
        } else {
            return new ResponseEntity(resultMap, HttpStatus.BAD_REQUEST);
        }
    }

    public void sendStatus(final String message, final String guid, final boolean hasFinished) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        logger.info("Sending status to " + guid + ": " + message);
        simpMessagingTemplate.convertAndSend("/topic/executions/" + guid, new ExecutionStatusWrapper(simpleDateFormat.format(new Date()), message, hasFinished));
    }

    // Health check for GCP
    @RequestMapping(value = "/_ah/health", method = RequestMethod.GET)
    public ResponseEntity gcpHealthCheck() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
