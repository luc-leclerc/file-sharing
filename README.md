# file-sharing

Simple web application that allows to list, upload and download files, up to 40G.

## Design decisions

* [Codegen template customization](#codegen-template-customization)
* [No security](#no-security)
* [No user interface](#no-user-interface)

### Codegen template customization

Default template is overwritten with files in `./template`.

* apiController.mustache: The controller will be implemented locally, using the interface generated, so the template for the Controller was emptied.
* api.mustache: Commented implementation details methods that I don't need from this interface (ex.: getObjectMapper(), getRequest(), etc)

### No security

Will be done through nginx sitting in front of this app. 

### No user interface

Used springdoc for the UI.