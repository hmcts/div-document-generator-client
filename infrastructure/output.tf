output "vaultName" {
    value = "${local.vaultName}"
}

output "vaultUri" {
    value = "${local.vaultUri}"
}

output "idam_s2s_url" {
    value = "http://${var.idam_s2s_url_prefix}-${local.local_env}.service.core-compute-${local.local_env}.internal"
}

output "environment_name" {
    value = "${local.local_env}"
}

output "auth_idam_client_secret" {
    value = "${data.azurerm_key_vault_secret.idam-secret.value}"
}
