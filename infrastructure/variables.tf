variable "reform_service_name" {
  default = "document-generator"
}

variable "reform_team" {
  default = "div"
}

variable "env" {
  type = "string"
}

variable "div_document_generator_port" {
  default = "4007"
}

variable "auth_provider_service_client_baseurl" {
  type = "string"
}

variable "auth_provider_service_client_microservice" {
  default = "divorce_ccd_submission"
}

variable "auth_provider_service_client_tokentimetoliveinseconds" {
  default = "900"
}

variable "pdf_service_url_part" {
  default = "cmc-pdf-service"
}

variable "evidence_management_client_api_url_part" {
  default = "div-em-client-api"
}

variable "evidence_management_client_api_health_endpoint" {
  default = "/health"
}

variable "subscription" {}

variable "location" {
  type    = "string"
  default = "UK South"
}

variable "ilbIp" {}

variable "vault_env" {}
