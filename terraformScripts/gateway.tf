resource "aws_api_gateway_rest_api" "doc_converter_api" {
  name        = "doc_converter_api"
  description = "API Gateway for the doc conversion function"
}

resource "aws_api_gateway_resource" "doc_converter_api_gateway" {
  rest_api_id = "${aws_api_gateway_rest_api.doc_converter_api.id}"
  parent_id   = "${aws_api_gateway_rest_api.doc_converter_api.root_resource_id}"
  path_part   = "${var.api_path}"
}

resource "aws_api_gateway_method" "doc_converter_method" {
  rest_api_id   = "${aws_api_gateway_rest_api.doc_converter_api.id}"
  resource_id   = "${aws_api_gateway_resource.doc_converter_api_gateway.id}"
  http_method   = "${var.doc_converter_http_method}"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "doc_converter_integration" {
  rest_api_id             = "${aws_api_gateway_rest_api.doc_converter_api.id}"
  resource_id             = "${aws_api_gateway_resource.doc_converter_api_gateway.id}"
  http_method             = "${aws_api_gateway_method.doc_converter_method.http_method}"
  integration_http_method = "${aws_api_gateway_method.doc_converter_method.http_method}"
  type                    = "AWS"
  uri                     = "arn:aws:apigateway:${var.region}:lambda:path/2015-03-31/functions/arn:aws:lambda:${var.region}:${var.account_id}:function:${aws_lambda_function.doc_converter_function.function_name}/invocations"
  credentials             = "arn:aws:iam::${var.account_id}:role/${aws_iam_role.lambda_apigateway_iam_role.name}"
}

resource "aws_api_gateway_method_response" "200" {
  rest_api_id = "${aws_api_gateway_rest_api.doc_converter_api.id}"
  resource_id = "${aws_api_gateway_resource.doc_converter_api_gateway.id}"
  http_method = "${aws_api_gateway_method.doc_converter_method.http_method}"

  response_models = {
    "application/json" = "Empty"
  }

  status_code = "200"
}

resource "aws_api_gateway_integration_response" "doc_converter_integration_response" {
  depends_on  = ["aws_api_gateway_integration.doc_converter_integration"]
  rest_api_id = "${aws_api_gateway_rest_api.doc_converter_api.id}"
  resource_id = "${aws_api_gateway_resource.doc_converter_api_gateway.id}"
  http_method = "${aws_api_gateway_method.doc_converter_method.http_method}"
  status_code = "${aws_api_gateway_method_response.200.status_code}"
}

resource "aws_api_gateway_deployment" "doc_converter_deploy" {
  depends_on  = ["aws_api_gateway_integration.doc_converter_integration"]
  stage_name  = "${var.api_env_stage_name}"
  rest_api_id = "${aws_api_gateway_rest_api.doc_converter_api.id}"
}

# Output the resource url that was assigned to this service deployment. This is the name you use
# defined in aws_api_gateway_deployment resource definition above

output "base_url" {
  value = "${aws_api_gateway_deployment.doc_converter_deploy.invoke_url}"
}


