# ComboCustomizer (a WIP...)
An HTML5 drag-and-drop combo customizer, simulating a custom building/crafting process for a pair of shoes.
Driven by Spring+Hibernate+WebSocket+angularJS.


Highlights:
- [x] HTML5 drag-and-drop UI, AJAX via Angular.
- [x] WebSocket to feed realtime execution status updates, with automatic fallback to XHR polling if WebSocket is unsupported.
- [x] Powered by SpringBoot on Jetty container.
- [x] REST API.
- [x] Hibernate ORM.
- [x] In-memory HSQLDB support via Hibernate.
- [x] Bundled HSQLDB server to facilitate connections (from outside SpringBoot application) to in-memory HSQLDB.
- [x] GCP GAE-ready.
- [ ] Seamless switching between Cloud SQL and in-memory HSQLDB.
