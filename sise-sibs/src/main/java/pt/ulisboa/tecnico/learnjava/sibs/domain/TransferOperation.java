package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	Services services;
	private int value;

	// States
	TransferOperationState registered;
	TransferOperationState deposited;
	TransferOperationState withdrawn;
	TransferOperationState completed;
	TransferOperationState canceled;
	TransferOperationState error;

	TransferOperationState transferOperationState;

	public TransferOperation(String sourceIban, String targetIban, int value, Services services)
			throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.registered = new SRegistered(this);
		this.deposited = new SDeposited(this);
		this.withdrawn = new SWithdrawn(this);
		this.completed = new SCompleted();
		this.canceled = new SCanceled();
		this.error = new SError();

		this.transferOperationState = this.registered;

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.services = services;
		this.value = value;
	}

	void setTransferOperationState(TransferOperationState newState) {
		this.transferOperationState = newState;
	}

	public void cancel() {
		this.transferOperationState.cancel();
	}

	public void process() {
		this.transferOperationState.process();
	}

	public TransferOperationState getCurrentState() {
		return this.transferOperationState;
	}

	public TransferOperationState getWithdrawnState() {
		return this.withdrawn;
	}

	public TransferOperationState getDepositedState() {
		return this.deposited;
	}

	public TransferOperationState getCompletedState() {
		return this.completed;
	}

	public TransferOperationState getCanceledState() {
		return this.canceled;
	}

	public TransferOperationState getErrorState() {
		return this.error;
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	public Services getService() {
		return this.services;
	}

	@Override
	public int commission() {
		return (int) Math.round(super.commission() + getValue() * 0.05);
	}

	public String getSourceIban() {
		return this.sourceIban;
	}

	public String getTargetIban() {
		return this.targetIban;
	}

}

////////////////////////////    ESTE CODIGO É REFERENTE AS PERGUNTAS 1 e 2 DA Parte 2 do Projecto /////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//public void process() throws AccountException, OperationException {
//
//	processVerificaitons(this.sourceIban, this.targetIban, this.value);
//
//	if (this.state.equals("Registered")) {
//		this.services.withdraw(this.sourceIban, this.value);
//		setState("Withdrawn");
//
//	} else if (this.state.equals("Withdrawn")
//			&& (this.targetIban.substring(0, 3).equals(this.sourceIban.substring(0, 3)))) {
//		this.services.deposit(this.targetIban, this.value);
//		setState("Completed");
//
//	} else if (this.state.equals("Withdrawn")) {
//		this.services.deposit(this.targetIban, this.value);
//		setState("Deposited");
//
//	} else if (this.state.equals("Deposited")) {
//		this.services.withdraw(this.sourceIban, commission());
//		setState("Completed");
//	}
//}
//
//public void processVerificaitons(String sourceIban, String targetIban, int value) throws OperationException {
//
//	if (sourceIban == null || targetIban == null || sourceIban.length() == 0 || targetIban.length() == 0) {
//		throw new OperationException("invalid account number");
//	}
//
//	if (this.services.getAccountByIban(sourceIban).getBalance() - value < 0) {
//		throw new OperationException("Not enough funds");
//
//	}
//
//	if (!targetIban.substring(0, 3).equals(sourceIban.substring(0, 3))
//			&& (this.services.getAccountByIban(sourceIban).getBalance() - value - commission() < 0)) {
//
//		throw new OperationException("Not enough funds for the transfer and the commision");
//
//	}
//
//}
//
//public void cancel() throws AccountException {
//
//	if (this.state.equals("Withdrawn")) {
//		this.services.deposit(this.sourceIban, this.value);
//
//	} else if (this.state.equals("Deposited")) {
//		this.services.deposit(this.sourceIban, this.value);
//		this.services.withdraw(this.targetIban, this.value);
//	}
//
//	setState("Cancelled");
//}
//
//public String getState() {
//	return this.state;
//}
//
//public void setState(String state) {
//	this.state = state;
