# Amazon-Search-Automation

This project automates the search functionality on an online shopping website (Amazon.in). It validates search behavior, result formatting, and sorting using a scalable Test Automation Framework built with Selenium and TestNG.

---

## 🎯 Objective  

Automate the process of searching for **smartphones under ₹30,000** and verify the **"Newest Arrivals" sorting functionality** on the selected e-commerce platform.

---

## 🛠️ Tech Stack & Tools  

| Technology / Tool | Purpose |
|------------------|---------|
| **Java (JDK 8+)** | Programming language |
| **Selenium WebDriver** | Browser automation |
| **TestNG** | Test execution, assertions, reporting, parallel execution |
| **Apache POI** | External test data handling via Excel |
| **Maven** | Project and dependency management |
| **Extent / TestNG Reports** | Test reporting |

---

## ✨ Key Features  

- 🧪 **Cross-Browser Testing:** Supports Chrome and Edge based on configuration  
- ⚡ **Parallel Execution:** Enabled using TestNG XML to reduce execution time  
- 📄 **Data-Driven Approach:** Writes test outputs dynamically in Excel using Apache POI  
- 🔍 **Dynamic Search Result Verification:** Validates search text, page counts, and result counts  
- 🔽 **Sorting Functionality Automation:** Validates available sorting options and selects **"Newest Arrivals"**  
- ⏱️ **Synchronization:** Uses explicit waits for stable test execution  
- 🔁 **Reusable Framework Components:** Modular Page Object Model structure (if applied)  
- 🛡️ **Robust Error Handling:** Graceful script failure handling with cleanup routines  

---

## 🧪 Automated Test Flow  

1. Launch browser using configuration settings  
2. Navigate to the Website URL (from config)  
3. Enter search term:  "mobile smartphones under 30000"
4. Validate:  
- Search string in results  
- Result metadata format (e.g., `1–24 of 1,000+ results`)  
- Pagination and item count  
5. Open **Sort By** dropdown and verify available options  
6. Select **“Newest Arrivals”** and verify selection  
7. Close the browser and generate report  

