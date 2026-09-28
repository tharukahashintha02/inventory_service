# Inventory Service

A small Java application used to exercise a CI/CD failure prediction gate.
It tracks stock for a warehouse: issuing and receiving items, and reporting
which lines have fallen to their reorder level.

The point of this repository is not the application. It is a realistic thing
for a pipeline to build, test and occasionally break, so that the risk gate has
genuine outcomes to learn from.

## Running it

    mvn test        # 13 JUnit tests
    mvn compile exec:java -Dexec.mainClass=com.nsbm.inventory.App

Or without Maven:

    javac -d out $(find src/main -name "*.java")
    java -cp out com.nsbm.inventory.App

## The risk gate

`.github/workflows/risk-gate.yml` calls a prediction service before the build
runs. The service scores the commit, the workflow applies a gating policy, and
the build outcome is reported back so the next prediction is better informed.

Set the repository secret `PREDICT_URL` to the address of the service.

Policy is set by the `POLICY` variable at the top of the workflow:

| Mode | Behaviour |
|---|---|
| `shadow` | Logs the prediction, never blocks |
| `warn` | Annotates high-risk builds |
| `block` | Fails the pipeline stage on high risk |

Under `block`, a commit whose message contains `[override-gate]` proceeds with
a warning. Without that escape hatch a failing pipeline could never recover,
because blocked builds never run and so never report a passing outcome.

## Breaking it on purpose

Each of these makes exactly one test fail:

| Change | File | Test that fails |
|---|---|---|
| `quantity <= reorderLevel` becomes `<` | `StockItem.java` | reorder boundary |
| Add `.reversed()` to the report sort | `Warehouse.java` | reorder report ordering |
| Change `if (updated < 0)` to `if (false)` | `StockItem.java` | issuing more than held |
