# Food Risk Scanner (FRS)
## AI Development Plan

---

# Project Overview

## Objective

Develop an offline-first Android application that allows users to:

- Scan food using CameraX
- Detect food using Google ML Kit
- Estimate calories and sugar
- Store daily food logs locally
- Calculate health risk
- Display weekly analytics
- Unlock achievements

Architecture:

- Kotlin
- MVVM
- Jetpack Compose (recommended) or XML
- Room Database
- CameraX
- Google ML Kit
- StateFlow
- Repository Pattern

---

# Phase 1 — Project Foundation

## Goal

Prepare project architecture before implementing features.

---

## Agent 1 — Project Setup

### Tasks

- Create Android project
- Configure Gradle
- Configure package structure
- Add required dependencies

### Dependencies

- Room
- CameraX
- ML Kit Image Labeling
- Navigation
- Lifecycle
- Coroutines
- Material3

### Folder Structure

```
app/

data/
    database/
    dao/
    entity/
    repository/

domain/
    model/
    usecase/

ui/
    home/
    scanner/
    analytics/
    profile/
    components/

ml/

navigation/

utils/
```

### Deliverables

- Project compiles
- No warnings
- Navigation works

Acceptance Criteria

- App launches
- Bottom navigation visible

---

# Phase 2 — Database Layer

## Goal

Create local persistence.

---

## Agent 2 — Database Design

### Create Entities

### UserProfile

```
id
name
age
weight
height
```

### FoodLog

```
id
foodName
calories
sugarContent
riskLevel
timestamp
imageUri
```

### Achievement

```
id
title
description
isUnlocked
```

---

### Create DAO

FoodLogDao

Functions

- insert()
- update()
- delete()
- getTodayFoods()
- getWeeklyFoods()
- getTotalCaloriesToday()

UserProfileDao

Functions

- getProfile()
- saveProfile()

AchievementDao

Functions

- getAchievements()
- unlock()

---

### Create Database

FoodRiskDatabase

Include

- FoodLogDao
- UserProfileDao
- AchievementDao

Deliverables

- Room migrations
- TypeConverters
- Database singleton

Acceptance Criteria

CRUD fully works.

---

# Phase 3 — Repository Layer

## Goal

Separate data source from ViewModel.

---

## Agent 3 — Repository

Repositories

FoodRepository

Methods

```
saveFood()

updateFood()

deleteFood()

getTodayFoods()

getWeeklyFoods()
```

ProfileRepository

AchievementRepository

Acceptance Criteria

No Room access directly from UI.

---

# Phase 4 — Navigation

## Goal

Create application navigation.

---

## Agent 4 — Navigation

Screens

```
Home

Scanner

Analytics
```

Navigation Flow

```
Home

↓

Scanner

↓

Result Bottom Sheet

↓

Save

↓

Home
```

Analytics accessible from Bottom Navigation.

Acceptance Criteria

Back stack works correctly.

---

# Phase 5 — Home Screen

## Goal

Implement dashboard.

---

## Agent 5 — Home UI

Components

- Greeting
- Daily calorie card
- Circular progress
- RecyclerView/LazyColumn
- Swipe delete

Data

Display

- today's calories
- today's foods

Acceptance Criteria

Realtime update after inserting food.

---

# Phase 6 — Scanner

## Goal

Capture food image.

---

## Agent 6 — Camera

Tasks

Configure

- CameraX Preview
- Capture
- Runtime permissions

Output

Captured Bitmap

Acceptance Criteria

User can capture image successfully.

---

# Phase 7 — ML Recognition

## Goal

Identify food.

---

## Agent 7 — ML Kit

Input

Bitmap

Output

```
Food Name

Confidence
```

If confidence low

Show

```
Unable to recognize.

Input manually.
```

Acceptance Criteria

Recognition returns label.

---

# Phase 8 — Manual Food Entry

## Goal

Fallback when ML fails.

---

## Agent 8 — Manual Input

UI

Fields

- Food Name
- Portion

Button

Save

Acceptance Criteria

Food saved without camera.

---

# Phase 9 — Nutrition Engine

## Goal

Estimate nutrition.

---

## Agent 9 — Nutrition Logic

Create local mapping

Example

```
Burger

Calories: 540

Sugar: 8g
```

```
Fried Chicken

Calories: 320

Sugar: 2g
```

```
Soft Drink

Calories: 180

Sugar: 38g
```

Logic

```
Food Name

↓

Lookup

↓

Calories

↓

Sugar
```

Acceptance Criteria

Known food returns nutrition.

---

# Phase 10 — Risk Scoring

## Goal

Calculate risk.

---

## Agent 10 — Risk Engine

Rules

Example

```
Calories < 300

LOW
```

```
300–600

MEDIUM
```

```
>600

HIGH
```

Additional sugar rules

```
Sugar > 25g

Increase one level
```

Output

```
LOW

MEDIUM

HIGH
```

Acceptance Criteria

Score always generated.

---

# Phase 11 — Result Bottom Sheet

## Goal

Display scan result.

---

## Agent 11 — Result UI

Display

- Image
- Food Name
- Calories
- Sugar
- Risk Badge

Buttons

Save

Cancel

Acceptance Criteria

Save inserts database record.

---

# Phase 12 — Weekly Analytics

## Goal

Visualize weekly consumption.

---

## Agent 12 — Analytics

Compute

```
7-day calories

7-day sugar

average

highest day
```

Display

- Line chart
- Bar chart

Acceptance Criteria

Updates automatically.

---

# Phase 13 — Achievement System

## Goal

Reward healthy habits.

---

## Agent 13 — Achievement Engine

Examples

Healthy Week

```
7 days below sugar limit
```

Balanced Diet

```
30 foods logged
```

Consistency

```
Use app for 14 days
```

Unlock automatically.

Acceptance Criteria

Achievements persist after restart.

---

# Phase 14 — ViewModels

## Goal

Business logic.

---

## Agent 14 — ViewModels

Create

```
HomeViewModel

ScannerViewModel

AnalyticsViewModel
```

Responsibilities

- expose StateFlow
- call repositories
- validation
- business rules

Acceptance Criteria

No business logic inside UI.

---

# Phase 15 — Dependency Injection

## Goal

Manage dependencies.

---

## Agent 15 — DI

Recommended

Hilt

Provide

- Database
- DAO
- Repository
- ViewModels

Acceptance Criteria

No manual singleton creation.

---

# Phase 16 — Testing

## Goal

Validate application.

---

## Agent 16 — Unit Testing

Test

Risk Engine

Nutrition Engine

Repository

DAO

ViewModel

Target

80% coverage.

---

# Phase 17 — UI Polish

## Goal

Improve UX.

---

## Agent 17 — UX

Implement

- Loading
- Empty state
- Error state
- Animations
- Snackbar
- Material3

Acceptance Criteria

Smooth navigation.

---

# Phase 18 — Final QA

## Goal

Production readiness.

---

## Agent 18 — QA

Checklist

✓ Camera permission

✓ Offline mode

✓ CRUD works

✓ Analytics correct

✓ Achievement unlock

✓ Rotation safe

✓ No crashes

✓ No memory leaks

✓ Database migration tested

✓ Performance acceptable

---

# Milestone Roadmap

## Milestone 1

Foundation

- Project
- Navigation
- Database

---

## Milestone 2

Core Features

- Camera
- ML
- Manual Input

---

## Milestone 3

Business Logic

- Nutrition
- Risk Score
- Save Food

---

## Milestone 4

User Experience

- Home
- Analytics
- Achievements

---

## Milestone 5

Production

- Testing
- Polish
- QA

---

# Suggested Git Branch Strategy

```
main

develop

feature/project-setup

feature/database

feature/repository

feature/navigation

feature/home

feature/scanner

feature/mlkit

feature/manual-input

feature/nutrition-engine

feature/risk-engine

feature/result-sheet

feature/analytics

feature/achievements

feature/testing

feature/ui-polish
```

---

# Definition of Done

A feature is complete only if:

- Code compiles without warnings.
- Unit tests pass.
- UI follows Material Design.
- State survives configuration changes.
- No business logic exists in UI components.
- Data persists correctly in Room.
- Error handling is implemented.
- Code is documented and formatted consistently.
- Feature integrates cleanly with MVVM architecture.