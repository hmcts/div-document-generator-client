output "vaultName" {
    value = "${local.vaultName}"
}

output "vaultUri" {
    value = "${local.vaultUri}"
}

output "test_environment" {
    value = "${local.local_env}"
}

output "idam_s2s_url" {
    value = "${local.idam_s2s_url}"
}

output "feature_resp_solicitor_details" {
  value = "${var.feature_resp_solicitor_details}"
}