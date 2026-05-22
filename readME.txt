Add Prometheus as a data source

1. Click the hamburger menu (☰) on the left
2. Click "Connections" → "Data Sources"
3. Click "Add new data source"
4. Search for "Prometheus" → click it
5. In the URL field type:  http://prometheus:9090
6. Scroll down → click "Save & Test"
7. You should see "Successfully queried the Prometheus API" ✅

Add Loki as a data source

1. Click "Add new data source" again
2. Search for "Loki" → click it
3. In the URL field type:  http://loki:3100
4. Scroll down → click "Save & Test"
5. You should see "Data source connected" ✅

Import the JVM dashboard (CPU, memory, threads)
1. Click hamburger menu (☰) → "Dashboards"
2. Click "New" → "Import"
3. In the "Import via grafana.com" field type:  4701
4. Click "Load"
5. In the "Prometheus" dropdown → select your Prometheus data source
6. Click "Import"


You now have a full JVM dashboard showing:
📊 Heap memory used vs available
📊 CPU usage (your Spring Boot process)
📊 JVM threads — how many are running, blocked, waiting
📊 Garbage collection pauses
📊 HTTP request rate


Import the Spring Boot dashboard (API metrics)
1. Dashboards → New → Import
2. Enter ID:  11378
3. Click "Load"
4. Select your Prometheus data source
5. Click "Import"

This shows:
📊 Which endpoints are being hit most
📊 Response times per endpoint (p50, p95, p99)
📊 Error rates (4xx, 5xx)


Step 9 — Verify database profiling
This one is automatic — P6Spy is already wired in from the dependency you added.
Just create a note via Postman and look at your Spring Boot console in IntelliJ.
You will see lines like this appearing:
10:23:45 | 3ms  | INSERT INTO notes (title, content, user_id) VALUES (?, ?, ?)
10:23:45 | 1ms  | SELECT * FROM users WHERE id = ?
Every SQL query your app runs will now show:

When it ran
How long it took in milliseconds
The actual SQL that was executed

If you see a query taking 200ms+ — that's a slow query you need to optimise.
