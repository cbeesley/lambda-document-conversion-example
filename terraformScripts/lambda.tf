# Java functions take a bit of ramp up time so increase timeout to 10 seconds
resource "aws_lambda_function" "doc_converter_function" {
  function_name = "doc_converter_function"
  s3_bucket = "com.thoughtpeak.codedeploy"
  s3_key    = "docconverter/document-converter-lambda-1.0-SNAPSHOT.jar"

  role             = "${aws_iam_role.lambda_apigateway_iam_role.arn}"
  handler          = "com.thoughtpeak.docconverter.DocConverterLambda::fileUploadHandler"
  memory_size      = "256"
  runtime          = "java8" 
  timeout          = "50"

 }

resource "aws_lambda_permission" "doc_converter_function" {
  statement_id  = "AllowExecutionFromApiGateway"
  action        = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.doc_converter_function.function_name}"
  principal     = "apigateway.amazonaws.com"
  source_arn    = "arn:aws:execute-api:${var.region}:${var.account_id}:${aws_api_gateway_rest_api.doc_converter_api.id}/${aws_api_gateway_deployment.doc_converter_deploy.stage_name}/${aws_api_gateway_integration.doc_converter_integration.integration_http_method}${aws_api_gateway_resource.doc_converter_api_gateway.path}"
}


output "lambdaarn" {
  value = "${aws_lambda_function.doc_converter_function.arn}"
}
