#PDF Document Converter Using AWS Lambda
##Introduction
This is a demonstration project on how to get a end to end document conversion pipeline setup using the Apache PDFBox java library and amazon web services lambdas. The AWS components used in this project are the following:

```
+--------------------+             +-----------------------+          +---------------+
|                    |             |                       |          |               |
|                    |             |                       |          |               |
|   Web API Gateway  +-----------> | Doc Converter Lambda  +--------> |  S3 Bucket    |
|                    |             |                       |          |               |
|                    |             |                       |          |               |
+--------------------+             +-----------------------+          +---------------+

```

The following programs are assumed already installed:

1. Apache Maven
1. Java 8 
1. AWS CLI Tools
1. (Optional) Terraform
1. (Optional)AWS SAM - for testing locally

There are two possible modes this type of lambda to convert a file:

* Direct conversion - This method posts a file to convert directly to the lambda function, via the API Gateway and then writes the file to the designated S3. There is a 10mb file size restriction on this method.
* Indirect conversion - This method posts a file directly to S3 using a signed URL, then triggers the lambda after all chunks of the file are uploaded.

This example demostrates the direct conversion. Because of the AWS imposed limit of file size for API Gateway, any file over 10mb on the direct conversion method will be rejected so clients need to check the file size before posting.

Depending on your system, you may opt to post files directly to S3 and thereby not restricted by the 10mb limit. This would make sense if the files are moved within AWS systems or you have users which can be authenticated by S3 directly. Due to security concerns its not ideal to provide a public bucket to write files in and you still need to authenticate users to make sure they can upload files, ensure they are named correctly, and a way to link user accounts to converted files. If you have a limited set of users, you can use the IAM roles to restrict access to your bucket 

Uploading large files via API Gateway in general could be another project itself which requires other AWS services. 

##Building the Java Library
Run the following mvn command to build the project:


```$ mvn package ```

The resulting .jar is the combined dependancies and is saved as project-dir/target/lambda-java-example-1.0-SNAPSHOT.jar. The .jar name is created by concatenating the artifactId and version in the pom.xml file.

The build creates this resulting .jar, using information in the pom.xml to do the necessary transforms. This is a standalone .jar (.zip file) that includes all the dependencies. This is your deployment package that you can upload to AWS Lambda to create a Lambda function.

To upload a a new jar to S3

```$ aws s3 cp target/lambda-java-example-1.0-SNAPSHOT.jar s3://com.example.yourbucket/docconverter/document-converter-lambda-1.0-SNAPSHOT.jar ```


###Refreshing the function code
You can refresh the function without destroying the entire config by copying the code/jar to the bucket (using the aws s3 cp command) then running:

```
$ aws lambda update-function-code --function-name doc_converter_function --s3-bucket com.example.yourbucket --s3-key docconverter/document-converter-lambda-1.0-SNAPSHOT.jar

```


##Running the DocConverter Lambda

To run the lambda in aws directly here are some ways to run it. Be aware this might incur charges on your aws account if you exceed the lambda execution free tier.

###Testing

To test invoke the lambda itself using the aws cli tools,

```aws lambda invoke --region=us-east-1 --function-name=hello_world_function output.txt```

Or send data

```aws lambda invoke --region=us-east-1 --function-name=hello_world_function --payload '{"contentType":"application/pdf","fileName":"sampleFile.txt","length":120849}' output.txt```

See the FileRequest pojo on the correct payload json.

With java based lambda's it seems there is a bit of startup time when you first trigger this lambda.  Sometimes, it will timeout on the first invocation, but will still be availble for use once tried again. Therefore, you might need to raise the timeout. 50 seconds seems to work well.

###Terraform
Terraform provides an easy way to get a full implemntation of this lambda going. You can use the AWS console to setup the lambda execution manually but Terraform can be a easier way to get things setup in a automated way.

Included are some scripts to get started in the terraform directory of this project.

###Prerequisites

Install the terraform binary and make it available on your path. You will also need to setup your AWS credentials in your home directory.
Chances are, if you have the AWS command tools working then this should be setup already. Terraform uses these credentials.

* Before you can work with a new configuration directory, it must be initialized using terraform init to create the state files by running this command in the 
terraform directory of this project:

     ```$ terraform init```
     
* Edit the vars.tf and within the account section, fill in your aws account address and the destination bucket you want the files to go to, and in lambda.tf, set the bucket name for where your jar will be stored in S3 along with the keyname 
     
* Now terraform will be ready, to deploy, supply your aws account id

     ``$ terraform apply`` 
     
     Type in 'yes' to proceed


To test the complete gateway to lambda, post data to the url that was printed out after running 'terraform apply'

```https://6j6a4ipi44.execute-api.us-east-1.amazonaws.com/beta/helloworld```

Each time you deploy a new gateway, AWS generates a new url which is printed upon completion of the terraform apply command.

If you are finished with the lambda, then you can delete everything by executing:

``$ terraform destroy``

If you are just testing, you should for certain remove everything as this test confirguration does no authentication on the API gateway and allows anyone with the URL to post files.