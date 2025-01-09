Inbound Messaging support:

Here is basically how it works:
  - a rar is created and deployed  (with pools and resources)
  - the mdb is defined with a rar referenced in the dd.
  - the mdb is deployed
  - the AS activates the message endpoints once its deployed
    (spec section 13.4.4)
  - once an endpoint is activated, AS calls rar via
    endpointActivation method and passes a MessageEndpointFactory 
    instance and ActivationSpec JavaBean.
  - rar used mef to create endpoint instances that it can deliver messages to
    Note: rar can pass XAResource while creating msg endpoint in order to 
          recieve transactional notifications.
    NOTE: AS must notify RA thru the XAResource if msg deliv is transacted.
  - when endpoint is deactivated, AS notifies RA via RA's endpointDeactivation()
    



