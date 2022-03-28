# Search Persister

Saves products to search index
___
## Local Build

Run the `build.sh` script to build the project locally

## Resources

This application uses [Terraform](https://www.terraform.io/) to create the necessary cloud resources.

The file [deploy.tf](deploy.tf) contains the description of all necessary resources with the necessary explanations.

Tango uses [Terraform Workspaces](https://www.terraform.io/docs/state/workspaces.html) to control multiple environments (dev, test, prod, etc). Follow below a simple way of using terraform commands:

```shell script
# First, initialize terraform, this will download the necessary remote states and modules used
$ terraform init

# Now you need to select the proper workspace, which represents an environment
$ terraform workspace select dev # or test, or int, or prod, or etc...

# The next step is to verify if the cloud resources are any different from your local files, terraform plan will show you the possible differences
$ terraform plan

# After analyzing the plan, you can apply this changes. Please be careful, this process should optimally be done exclusively from CICD
$ terraform apply  
```
