terraform {
  backend "azurerm" {}

  required_providers {
      azurerm = {
          source  = "hashicorp/azurerm"
          version = "3.117.1"
      }
      random = {
          source = "hashicorp/random"
      }
  }
}
