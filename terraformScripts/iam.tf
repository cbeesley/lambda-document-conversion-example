# Set the policy on the api gateway so that it can run the lambda
resource "aws_iam_role" "lambda_apigateway_iam_role" {
  name = "lambda_apigateway_iam_role"

  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": ["apigateway.amazonaws.com","lambda.amazonaws.com"]
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
POLICY
}
# Define the policy for the lambda execution, the access to the cloudwatch logs as well as access to the
# S3 bucket is assigned here
resource "aws_iam_role_policy" "lambda_policy" {
  name = "lambda_policy"
  role = "${aws_iam_role.lambda_apigateway_iam_role.id}"

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "lambda:InvokeFunction"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "cloudwatch:Describe*",
        "cloudwatch:Get*",
        "cloudwatch:List*"
      ],
      "Resource": "*"
    },
    {
       "Sid": "DestinationBucketPutObjectAccess",
       "Effect": "Allow",
       "Action": [
         "s3:PutObject"
       ],
       "Resource": "arn:aws:s3:::${s3_target_bucket_name}/convertedDocs/*"
    }
  ]
}
POLICY
}
