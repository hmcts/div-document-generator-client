locals {
  ase_name = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"

  evidence_management_client_api_url = "http://${var.evidence_management_client_api_url_part}-${var.env}.service.${local.ase_name}.internal"
  pdf_service_url                    = "http://${var.pdf_service_url_part}-${var.env}.service.${local.ase_name}.internal"
  idam_s2s_url                       = "http://${var.idam_s2s_url_prefix}-${local.local_env}.service.core-compute-${local.local_env}.internal"
  local_env                          = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  dm_store_url                       = "http://dm-store-${local.local_env}.service.core-compute-${local.local_env}.internal"

  previewVaultName                   = "${var.product}-${var.reform_service_name}"
  nonPreviewVaultName                = "${var.reform_team}-${var.reform_service_name}-${var.env}"
  vaultName                          = "${var.env == "preview" ? local.previewVaultName : local.nonPreviewVaultName}"

  nonPreviewVaultUri = "${module.key-vault.key_vault_uri}"
  previewVaultUri = "https://div-${var.reform_service_name}-aat.vault.azure.net/"
  vaultUri = "${var.env == "preview"? local.previewVaultUri : local.nonPreviewVaultUri}"
}

module "div-dgs" {
  source       = "git@github.com:hmcts/moj-module-webapp.git?ref=master"
  product      = "${var.product}-${var.reform_service_name}"
  location     = "${var.location}"
  env          = "${var.env}"
  ilbIp        = "${var.ilbIp}"
  subscription = "${var.subscription}"
  is_frontend  = false

  app_settings = {
    REFORM_SERVICE_NAME                                   = "${var.reform_service_name}"
    REFORM_TEAM                                           = "${var.reform_team}"
    REFORM_ENVIRONMENT                                    = "${var.env}"
    AUTH_PROVIDER_SERVICE_CLIENT_BASEURL                  = "${local.idam_s2s_url}"
    AUTH_PROVIDER_SERVICE_CLIENT_MICROSERVICE             = "${var.auth_provider_service_client_microservice}"
    AUTH_PROVIDER_SERVICE_CLIENT_KEY                      = "${data.vault_generic_secret.div-doc-s2s-auth-secret.data["value"]}"
    AUTH_PROVIDER_SERVICE_CLIENT_TOKENTIMETOLIVEINSECONDS = "${var.auth_provider_service_client_tokentimetoliveinseconds}"
    PDF_SERVICE_BASEURL                                   = "${local.pdf_service_url}"
    EVIDENCE_MANAGEMENT_CLIENT_API_BASEURL                = "${local.evidence_management_client_api_url}"
    EVIDENCE_MANAGEMENT_CLIENT_API_HEALTH_ENDPOINT        = "${var.evidence_management_client_api_health_endpoint}"
  }
}

# region save DB details to Azure Key Vault
module "key-vault" {
  source              = "git@github.com:hmcts/moj-module-key-vault?ref=master"
  name                = "${local.vaultName}"
  product             = "${var.product}"
  env                 = "${var.env}"
  tenant_id           = "${var.tenant_id}"
  object_id           = "${var.jenkins_AAD_objectId}"
  resource_group_name = "${module.div-dgs.resource_group_name}"

  # dcd_cc-dev group object ID
  product_group_object_id = "1c4f0704-a29e-403d-b719-b90c34ef14c9"
}

provider "vault" {
  address = "https://vault.reform.hmcts.net:6200"
}

data "vault_generic_secret" "div-doc-s2s-auth-secret" {
  path = "secret/${var.vault_env}/ccidam/service-auth-provider/api/microservice-keys/divorceDocumentGenerator"
}

resource "azurerm_key_vault_secret" "div-doc-s2s-auth-secret" {
  name      = "div-doc-s2s-auth-secret"
  value     = "${data.vault_generic_secret.div-doc-s2s-auth-secret.data["value"]}"
  vault_uri = "${module.key-vault.key_vault_uri}"
}
