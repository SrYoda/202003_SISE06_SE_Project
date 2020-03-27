package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class MBWayController {
	private MBWayAccount model;
	private MBWayAccountView view;
	public Services services;

	HashMap<String, Integer> transfersToBePerformed = new HashMap<String, Integer>();
	private String yourPhoneNumber;
	private String billPhoneNumber;

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

	public MBWayAccount getModel() {
		return this.model;
	}

	public MBWayAccountView getView() {
		return this.view;
	}

	public int numberOfFriends() {
		return this.model.numberOfFriends();
	}

	public boolean verifyIfFriend(String yourPhoneNumber, String friendPhoneNumber) {
		MBWayAccount account = this.model.getAnAccount(yourPhoneNumber);
		return account.verifyIfFriend(friendPhoneNumber);
	}

	// More functions that need to be implemented for the project
	public void associateMbWay(String Iban, String phoneNumber) {

		setPhoneNumber(phoneNumber);
		setIban(Iban);

		this.model.generateActivationCode();
		this.view.activationCode(this.model.getActivationCode());

	}

	public void confirmMbWay(String inputCode) {
		if (inputCode.equals(this.model.getActivationCode())) {
			this.model.addAccount(this.model.getPhonenumber(), this.model);
			this.view.validActivationCode();
		} else {
			this.view.wrongActivationCode();
			;
		}
	}

	public void MBWayTransfer(String sourcePhoneNumber, String targetPhoneNumber, int amount) throws AccountException {

		String sourceIban = this.model.getAnAccount(sourcePhoneNumber).getIban();
		String targetIban = this.model.getAnAccount(targetPhoneNumber).getIban();
		System.out.println(targetIban);

		if (this.services.getAccountByIban(sourceIban).getBalance() - amount < 0) {
			this.view.youAreBroke();

		} else {
			this.services.withdraw(sourceIban, amount);
			this.services.deposit(targetIban, amount);
			this.view.successfulTransfer();
		}
	}

	public void MBWaySplitBill(int counterOfFriends, int bill) throws AccountException {
		MBWayAccount userAccount = this.model.getAnAccount(this.yourPhoneNumber);

		if (userAccount.numberOfFriends() < counterOfFriends) {
			this.view.oneFirendIsMissing();
		} else if (userAccount.numberOfFriends() > counterOfFriends) {
			this.view.tooManyFriends();
		} else {
			int soma = 0;
			for (String phonenumber : this.transfersToBePerformed.keySet()) {
				if (phonenumber.equals(this.billPhoneNumber)) {
				} else {
					soma += this.transfersToBePerformed.get(phonenumber);
				}
			}
			if (soma != bill) {
				this.view.wrongBill();
			} else {
				for (String phonenumber : this.transfersToBePerformed.keySet()) {
					System.out.println(phonenumber);

					if (phonenumber.equals(this.billPhoneNumber)) {
					} else {
						MBWayTransfer(phonenumber, "555555555", this.transfersToBePerformed.get(phonenumber));
					}

				}
			}

		}
	}

	public void setTargetPhoneNumber(String phoneNumber) {
		this.billPhoneNumber = phoneNumber;
	}

	public void setYourPhoneNumber(String phoneNumber) {
		this.yourPhoneNumber = phoneNumber;
	}

	public void addTransfer(String phoneNumber, int amount) {
		verifyIfUserOfMBWay(phoneNumber);

		if (this.transfersToBePerformed.size() < 2) {
			this.transfersToBePerformed.put(phoneNumber, amount);
		} else {
			if (verifyIfFriend(this.yourPhoneNumber, phoneNumber)) {
				this.transfersToBePerformed.put(phoneNumber, amount);
			} else {
				this.view.yourFriendsDoesNotUsesMBWay();
			}
		}

	}

	public Boolean verifyIfUserOfMBWay(String phoneNumber) {
		return this.model.getAnAccount(phoneNumber) != null;
	}

	public void addFriend(String yourPhoneNumber, String friendPhoneNumber) {
		MBWayAccount account = this.model.getAnAccount(yourPhoneNumber);
		account.addFriend(friendPhoneNumber);
		this.model.updateAccount(yourPhoneNumber, account);
		this.view.succesfullAddFriend();
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
