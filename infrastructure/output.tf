output "vaultName" {
    value = "${module.key-vault.key_vault_name}"
}

output "vaultUri" {
    value = "${module.key-vault.key_vault_uri}"
}

output "idam_s2s_url" {
    value = "http://${var.idam_s2s_url_prefix}-${var.env}.service.${local.ase_name}.internal"
}
