locals {
  ase_name                           = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"
  evidence_management_client_api_url = "http://${var.evidence_management_client_api_url_part}-${var.env}.service.${local.ase_name}.internal"

  #tactical
  #evidence_management_client_api_url = "http://betaDevBdivorceAppLB.reform.hmcts.net:4016"
  pdf_service_url = "http://${var.pdf_service_url_part}-${var.env}.service.${local.ase_name}.internal"
}

module "div-document-generator" {
  source       = "git@github.com:hmcts/moj-module-webapp.git?ref=master"
  product      = "${var.reform_team}-${var.reform_service_name}"
  location     = "${var.location}"
  env          = "${var.env}"
  ilbIp        = "${var.ilbIp}"
  subscription = "${var.subscription}"
  is_frontend  = false

  app_settings = {
    REFORM_SERVICE_NAME                                   = "${var.reform_service_name}"
    REFORM_TEAM                                           = "${var.reform_team}"
    REFORM_ENVIRONMENT                                    = "${var.env}"
    DIV_DOCUMENT_GENERATOR_PORT                           = "${var.div_document_generator_port}"
    AUTH_PROVIDER_SERVICE_CLIENT_BASEURL                  = "${var.auth_provider_service_client_baseurl}"
    AUTH_PROVIDER_SERVICE_CLIENT_MICROSERVICE             = "${var.auth_provider_service_client_microservice}"
    AUTH_PROVIDER_SERVICE_CLIENT_KEY                      = "${data.vault_generic_secret.auth_provider_service_client_key.data["value"]}"
    AUTH_PROVIDER_SERVICE_CLIENT_TOKENTIMETOLIVEINSECONDS = "${var.auth_provider_service_client_tokentimetoliveinseconds}"
    //PDF_SERVICE_BASEURL                                   = "${local.pdf_service_url}"
    PDF_SERVICE_BASEURL                                   = "${var.pdf_service_baseurl}"
    EVIDENCE_MANAGEMENT_CLIENT_API_BASEURL                = "${local.evidence_management_client_api_url}"
    EVIDENCE_MANAGEMENT_CLIENT_API_HEALTH_ENDPOINT        = "${var.evidence_management_client_api_health_endpoint}"
  }
}

provider "vault" {
  address = "https://vault.reform.hmcts.net:6200"
}

data "vault_generic_secret" "auth_provider_service_client_key" {
  path = "secret/${var.vault_env}/ccidam/service-auth-provider/api/microservice-keys/divorceDocumentGenerator"
}
