# 🚀 Solidgate Test Automation Project

This project contains automated UI and API tests for the Solidgate payment system, built with **Java**, **Maven**, **Selenide**, and **REST Assured**.

## ✨ Technologies Used

* **Java 11+**: Primary programming language.
* **Maven**: Build automation and project management.
* **Selenide**: UI test automation framework.
* **REST Assured**: API testing library.
* **TestNG**: Test framework.
* **Guava**: Utility library (for HMAC-SHA512).

## 🛠️ Local Setup

1.  **Clone the repository**:
    ```bash
    git clone <YOUR_REPO_URL>
    cd solidgate_task
    ```
2.  **Install Java Development Kit (JDK) 17+** and **Maven**.
3.  **Open in IntelliJ IDEA**: Import `pom.xml` as a Maven project.
4.  **Sync Maven dependencies**.
5.  **Configure `src/test/resources/test.properties`**:
    * **Keep `secret.key` and `public.key` as placeholders** (`YOUR_SECRET_KEY_HERE`, `YOUR_PUBLIC_KEY_HERE`).

## 🔒 Handling Sensitive Data (API Keys)

**DO NOT COMMIT** your actual Solidgate Secret Key and Public Key to Git. Instead, use **environment variables**:

* **SOLIDGATE_SECRET_KEY**: Set your actual Secret Key as an environment variable.
* **SOLIDGATE_PUBLIC_KEY**: Set your actual Public Key as an environment variable.

The project automatically loads these keys from environment variables if placeholders are found in `test.properties`.

