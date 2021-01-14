variable "reform_service_name" {
  default = "dgs"
}

variable "reform_team" {
  default = "div"
}

variable "capacity" {
    default = "1"
}

variable "instance_size" {
    default = "I2"
}

variable "component" {}

variable "env" {}

variable "product" {}

variable "raw_product" {
  default = "div"
}

variable "tenant_id" {}

variable "jenkins_AAD_objectId" {
  description = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}

variable "appinsights_instrumentation_key" {
    description = "Instrumentation key of the App Insights instance this webapp should use. Module will create own App Insights resource if this is not provided"
    default = ""
}

variable "idam_s2s_url_prefix" {
  default = "rpe-service-auth-provider"
}

variable "auth_provider_service_client_microservice" {
  default = "divorce_document_generator"
}

variable "auth_provider_service_client_tokentimetoliveinseconds" {
  default = "900"
}

variable "pdf_service_url_part" {
  default = "cmc-pdf-service"
}

variable "evidence_management_client_api_url_part" {
  default = "div-emca"
}

variable "evidence_management_client_api_health_endpoint" {
  default = "/health"
}

variable "subscription" {}

variable "location" {
  default = "UK South"
}

variable "ilbIp" {}

variable "vault_env" {
    default = "preprod"
}

variable "common_tags" {
    type = map(string)
}

variable "health_check_ttl" {
    default = "5000"
}

variable "feature_resp_solicitor_details" {
  default = "false"
}

