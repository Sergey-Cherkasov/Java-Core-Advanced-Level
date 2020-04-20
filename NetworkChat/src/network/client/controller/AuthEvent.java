package network.client.controller;

@FunctionalInterface
public interface AuthEvent {
   void authIsSuccessful(String userName);
}
