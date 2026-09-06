# OOPROG - Inventory Management System

**Midterm Project | 2nd Year - 1st Semester**

A Java-based Inventory Management System developed for the OOPROG midterm project. This project demonstrates **Encapsulation** and **Abstraction** while allowing users to manage items in a store's inventory.

## Features

- Add Item
- Update Item Quantity or Price
- Remove Item
- Display Items by Category
- Display All Items
- Search Item
- Sort Items by Quantity or Price
- Sort in Ascending or Descending Order
- Display Low Stock Items
- Input Validation and Error Handling

## Item Details

Each item contains:

- ID
- Name
- Quantity
- Price
- Category

### Categories

- Clothing
- Electronics
- Entertainment

## Validations

The system validates:

- Menu choices
- Categories
- Item IDs
- Duplicate IDs
- Item names
- Quantities
- Prices
- Update inputs
- Sort options
- Empty and invalid inputs

## OOP Concepts

### Encapsulation
Item attributes are private and accessed through getters and setters.

### Abstraction
Inventory operations and user interaction are separated into different classes, allowing users to manage the inventory without needing to know the internal implementation.

## Class Structure

| File | Description |
|---|---|
| `Item.java` | Stores and manages item information |
| `Inventory.java` | Handles inventory operations |
| `InventorySystem.java` | Handles menus, user input, and output |

## Technologies

- Java
- Visual Studio Code
- Git
- GitHub