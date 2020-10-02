provider "azurerm" {
    features {}
}

locals {
  aseName = "core-compute-${var.env}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"

  evidence_management_client_api_url = "http://${var.evidence_management_client_api_url_part}-${local.local_env}.service.core-compute-${local.local_env}.internal"
  pdf_service_url                    = "http://${var.pdf_service_url_part}-${local.local_env}.service.core-compute-${local.local_env}.internal"
  idam_s2s_url                       = "http://${var.idam_s2s_url_prefix}-${local.local_env}.service.core-compute-${local.local_env}.internal"

  previewVaultName = "${var.reform_team}-aat"
  nonPreviewVaultName = "${var.reform_team}-${var.env}"
  vaultName = "${var.env == "preview" ? local.previewVaultName : local.nonPreviewVaultName}"
  vaultUri = data.azurerm_key_vault.div_key_vault.vault_uri

  asp_name = "${var.env == "prod" ? "div-dgs-prod" : "${var.raw_product}-${var.env}"}"
  asp_rg = "${var.env == "prod" ? "div-dgs-prod" : "${var.raw_product}-${var.env}"}"
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.component}-${var.env}"
  location = var.location
}

data "azurerm_key_vault" "div_key_vault" {
    name                = local.vaultName
    resource_group_name = local.vaultName
}

data "azurerm_key_vault_secret" "div-doc-s2s-auth-secret" {
    name      = "div-doc-s2s-auth-secret"
    key_vault_id = data.azurerm_key_vault.div_key_vault.id
}

data "azurerm_key_vault_secret" "idam-secret" {
    name      = "idam-secret"
    key_vault_id = data.azurerm_key_vault.div_key_vault.id
}
