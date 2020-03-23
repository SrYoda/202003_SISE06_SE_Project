package pt.ulisboa.tecnico.learnjava.sibs.domain;

public class MBWayAccountView {

	public void printMBWayAccountDetails(String phonenumber, String Iban) {
		System.out.println("Accont: ");
		System.out.println("PhoneNumber: " + phonenumber);
		System.out.println("IBAN: " + Iban);
	}

	public void askForIban() {
		System.out.println("Please input the Iban Account number: ");
	}

	public void askForPhoneNumber() {
		System.out.println("Please input your phone number: ");
	}

	public void askForFriendsPhoneNumber() {
		System.out.println("Please input your friends phone number: ");
	}

	public void yourFriendsDoesNotUsesMBWay() {
		System.out.println("Please input your friends phone number: ");
	}

	public void askForTargetPhoneNumber() {
		System.out.println("Please input the target phone number: ");
	}

	public void noMoneyFriends() {
		System.out.println("Oh no! One friend does not have money to pay!");
	}

	public void tooManyFriends() {
		System.out.println("Oh no! Too many friends.");
	}

	public void oneFirendIsMissing() {
		System.out.println("Oh no! One friend is missing.");
	}

	public void wrongBill() {
		System.out.println("Something is wrong. Did you set the bill amount right?");
	}

	public void successfulBillPayment() {
		System.out.println("Bill payed successfully!");

	}

	public void askForAmount() {
		System.out.println("Please input the amount");
	}

	public void askForBill() {
		System.out.println("Please input the total Amount Of the Bill");
	}

	public void successfulTransfer() {
		System.out.println("Transfer performed successfully!");
	}

	public void youAreBroke() {
		System.out.println("Not enough money on the source account");
	}

	public void menuOptions() {
		System.out.println(
				"** Thank you for using MBWay** \n \n \n Please input one of the choices\n 1 - Subscrite to our service\n 2 - confirm your account\n 3 - Make a transfer\n 4 - Split Bill \n 5 - Add a Friend \n 0 -Exit \n Your Option :   ");
	}

	public void pleaseSummitActivationCode() {
		System.out.println("\n Please Input Your Activation Code:  ");
	}

	public void activationCode(String code) {
		System.out.println("\n\n\n This is your confirmation code, please keep it safe   " + code + "\n\n");
	}

	public void wrongActivationCode() {
		System.out.print("The code does not match, please try again");
	}

	public void validActivationCode() {
		System.out.print("Your account has been added to our services");

	}

	public void invalidInputMenu() {
		System.out.println("Your input is not Valid please try again");
	}

}
