# Problem Overview

Modern payment platforms integrate with multiple payment gateways, each having different:

- Commission structures (fixed + percentage)

- Processing times (Instant, Hours, Next-day)

- Daily quota limits

- Minimum / Maximum transaction limits

- Availability windows

## Selecting the wrong gateway may lead to:

- Higher operational cost

- Transaction delays

- Quota violations

# Challenge:
- is to design and implement a Smart Payment Routing Engine that dynamically selects the most optimal gateway.
  
# Solution Overview

Instead of using hard-coded comparison logic, the system implements a dynamic weighted scoring engine.

Gateway selection is driven by configurable scoring factors stored in the database and evaluated at runtime.

This makes the routing engine:

- Flexible

- Configurable without code changes

# Core Idea

Each gateway is evaluated using multiple scoring factors such as:

- Commission Cost

- Processing Time

- Remaining Daily Quota

Each factor produces a raw numeric score.

The engine then:

- Applies Min-Max normalization

- Adjusts score direction (LOWER_BETTER / HIGHER_BETTER)

- Applies configurable weights per factor

- Calculates a final weighted score

- Ranks gateways descending by total score

- Final Score Formula: Final Score = Σ (Weight × NormalizedFactor) / Σ (Weights)


The highest-scoring gateway is selected, and the rest are returned as ranked alternatives.

# Design
## scoring Engine

Responsible for:

- Fetching active factor configurations from DB

- Computing min/max values per factor

- Normalizing scores

- Applying weights

- Ranking gateways

## Factor Registry

Each scoring factor implements: public interface Scoring Factor

This allows:

- Adding new factors without modifying the engine

- Open/Closed Principle compliance

- Clean separation of concerns

- Scoring Factor ConfigService

Loads factor configuration dynamically from the database:

- Factor code

- Weight


This enables runtime tuning without redeploying the system.

- If HIGHER_BETTER:
(value - min) / (max - min)

- If LOWER_BETTER:
(max - value) / (max - min)


- Scores are shifted to a safe range (0.1 → 0.9) to prevent:

Zero dominance

Extreme bias when values are identical

- If min == max → default score = 0.5

## Routing Flow

The routing process follows a two-stage decision model.

### Phase 1: Hard Filtering (Elimination Stage)

Gateways must satisfy strict validation rules:

- Min / Max transaction limits

- Availability window

- Daily quota remaining

- Urgency compatibility

- Enabled flag

Invalid gateways are eliminated before scoring.

If none remain → NotFountException is thrown.

### Phase 2: Soft Optimization (Scoring Stage)

- Remaining gateways are enriched with:

- Calculated commission

- Remaining quota

- Numeric processing time

Then passed to:scoring engine.rank to rank every gateway based on Their factories, The engine ranks them using weighted normalized scoring.

<img width="492" height="396" alt="flow_top_section" src="https://github.com/user-attachments/assets/b561ca38-a936-4cef-8bd1-6e6c34af1556" />

# End-to-End Execution Flow
- Client Request 
- RoutingService->recommend
- Load enabled gateways     
- Apply hard filters   
- Rank Gateways     
- Select best gateway     
- Log transaction
- Return recommendation + alternatives

# Back-End App
- First please compose the docker file that attached into repo to run and install database you can change the port, username and password as you like but don't
    to change it in properties file in the project using
  ```bash
  docker compose up
  ```
- Second run the project ob back-end from any IDE For spring boot like Intelj

- Then run the sql query that was in scripts.txt for sequence id

# Front-End App

## Development server

To start a local development server, run:

```bash
ng serve (or)
npm start
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.
