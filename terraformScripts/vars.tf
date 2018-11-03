variable "region" {
    default="us-east-1"
}
# Need to provide your account id in order for this to work
variable "account_id" {
    default="<ENTER YOUR ID>"
}

variable "lambda_payload_filename" {
  default = "../docconverter/target/document-converter-lambda-1.0-SNAPSHOT.jar"
}

variable "lambda_function_handler" {
  default = "com.thoughtpeak.docconverter.DocConverterLambda"
}

variable "lambda_runtime" {
  default = "java8"
}
# This will be the uri that you need to include in aws generated url such as
# https://1234sdsds.execute-api.us-east-1.amazonaws.com/beta/docconverter
variable "api_path" {
  default = "docconverter"
}

variable "doc_converter_http_method" {
  default = "POST"
}

variable "api_env_stage_name" {
  default = "beta"
}

variable "s3_target_bucket_name" {
   default = "<NAME OF YOUR BUCKET TO STORE THE RESULTING FILES>"
}
