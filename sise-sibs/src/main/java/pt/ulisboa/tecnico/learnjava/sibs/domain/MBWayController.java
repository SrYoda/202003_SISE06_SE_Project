package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class MBWayController {
	private MBWayAccount model;
	private MBWayAccountView view;
	public Services services;

	public MBWayController(MBWayAccount model, MBWayAccountView view, Services services) {
		this.model = model;
		this.view = view;
		this.services = services;
	}

	public void setPhoneNumber(String phonenumber) {
		this.model.setPhonenumber(phonenumber);
	}

	public String getPhoneNumber() {
		return this.model.getPhonenumber();
	}

	public void setIban(String Iban) {
		this.model.setIban(Iban);
	}

	public String getIban() {
		return this.model.getIban();
	}

	public void addAccount(String phoneNumber, MBWayAccount account) {
		this.model.addAccount(phoneNumber, account);
	}

	public MBWayAccount getAccount(String phoneNumber) {
		return this.model.getAnAccount(phoneNumber);
	}

	public int numberOfFriends() {
		return this.model.numberOfFriends();
	}

	public boolean verifyIfFriend(String friendPhoneNumber) {
		return this.model.verifyIfFriend(friendPhoneNumber);
	}

	// More functions that need to be implemented for the project
	public void associateMbWay() {

		this.view.askForIban();
		Scanner ib = new Scanner(System.in);
		String Iban = ib.nextLine();

		this.view.askForPhoneNumber();
		Scanner pn = new Scanner(System.in);
		String phonenumber = pn.nextLine();

		setPhoneNumber(phonenumber);
		setIban(Iban);

		this.model.generateActivationCode();
		this.view.activationCode(this.model.getActivationCode());

	}

	public void confirmMbWay() {
		this.view.pleaseSummitActivationCode();
		Scanner cd = new Scanner(System.in);
		String inputCode = cd.nextLine();
		if (inputCode.equals(this.model.getActivationCode())) {
			this.model.addAccount(this.model.getPhonenumber(), this.model);
			this.view.validActivationCode();

		} else {
			this.view.wrongActivationCode();
			;
		}
	}

	public void MBWayTransfer() throws AccountException {
		this.view.askForPhoneNumber();
		Scanner srPN = new Scanner(System.in);
		String sourcePhoneNumber = srPN.nextLine();

		this.view.askForTargetPhoneNumber();
		Scanner tgPN = new Scanner(System.in);
		String targetPhoneNumber = tgPN.nextLine();

		this.view.askForAmount();
		Scanner amt = new Scanner(System.in);
		int amount = amt.nextInt();

		String sourceIban = this.model.getAnAccount(sourcePhoneNumber).getIban();
		String targetIban = this.model.getAnAccount(targetPhoneNumber).getIban();

		if (this.services.getAccountByIban(sourceIban).getBalance() - amount < 0) {
			this.view.youAreBroke();
		} else {
			this.services.withdraw(sourceIban, amount);
			this.services.deposit(targetIban, amount);
			this.view.successfulTransfer();
			System.out.println(this.services.getAccountByIban(sourceIban).getBalance());
			System.out.println(this.services.getAccountByIban(targetIban).getBalance());
		}
	}

	public void MBWaySplitBill() throws AccountException {

		this.view.askForTargetPhoneNumber();
		Scanner responsiblePN = new Scanner(System.in);
		String targetPhoneNumber = responsiblePN.nextLine();
		responsiblePN.close();

		this.view.askForBill();
		Scanner b = new Scanner(System.in);
		int bill = b.nextInt();
		b.close();

		int numberOfFriends = this.model.numberOfFriends();

		int soma = 0;
		int counterOfFriends = 0;

		int menuOption = 0;

		while (0 != menuOption) { // PAssar o While para o MVC PAttern

			counterOfFriends += 1;
			HashMap<String, Integer> transfersToBePerformed = new HashMap<String, Integer>();

			// Input friends phone Number
			Scanner friend = new Scanner(System.in);
			String friendPhoneNumber = friend.nextLine();

			// Validates that the friend uses the Services
			if (verifyIfFriend(friendPhoneNumber)) {
				this.view.yourFriendsDoesNotUsesMBWay();
				menuOption = 0;
			}

			this.view.askForAmount();
			Scanner a = new Scanner(System.in);
			int amount = a.nextInt();

			// Validates that the friends has money enough to pay
			if (this.services.getAccountByIban(getAccount(friendPhoneNumber).getIban()).getBalance() - amount < 0) {
				this.view.noMoneyFriends();
				menuOption = 0;
			}

			soma += amount;

			if (soma < bill) {
				transfersToBePerformed.put(friendPhoneNumber, amount);
			}

			if (soma == bill) {

				transfersToBePerformed.put(friendPhoneNumber, amount);

				for (Map.Entry mapElement : transfersToBePerformed.entrySet()) {
					this.services.withdraw((String) mapElement.getKey(), (int) mapElement.getValue());
					this.services.deposit(targetPhoneNumber, soma);
					menuOption = 0;
					this.view.successfulBillPayment();
				}
			}

			// Verifies if it is introduzing more friends than expected
			if (counterOfFriends > numberOfFriends) {
				this.view.tooManyFriends();
				menuOption = 0;
			}
		}
		if (counterOfFriends < numberOfFriends) {
			this.view.oneFirendIsMissing();
		}
		if (soma < bill) {
			this.view.wrongBill();
		}
	}

	public void addFriend() {
		this.view.askForPhoneNumber();
		Scanner newFriendPhoneNumber = new Scanner(System.in);
		String friendPhoneNumber = newFriendPhoneNumber.nextLine();
		newFriendPhoneNumber.close();
		this.model.addFriend(friendPhoneNumber);
	}

	// Functions Realated with the display of information
	public void updateView() {
		this.view.printMBWayAccountDetails(this.model.getIban(), this.model.getPhonenumber());
	}

	public void viewMenu() {
		this.view.menuOptions();
	}

	public void viewInvalidInputMenu() {
		this.view.invalidInputMenu();
	}

}
