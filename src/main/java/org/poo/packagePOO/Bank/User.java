package org.poo.packagePOO.Bank;

import org.poo.packagePOO.Bank.Account.ServicePlan.ServicePlan;
import org.poo.packagePOO.Bank.Account.ServicePlan.StandardPlan;
import org.poo.packagePOO.Bank.Account.ServicePlan.StudentPlan;

public class User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String birthDate;
    private final String occupation;
    private ServicePlan servicePlan;

    /**
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param birthDate
     * @param occupation
     */
    public User(final String firstName, final String lastName,
                final String email, final String birthDate,
                final String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.occupation = occupation;
        if(occupation.equals("student")) {
            this.servicePlan = new StudentPlan();
        } else {
            this.servicePlan = new StandardPlan();
        }
    }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @return
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     *
     * @return
     */
    public ServicePlan getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(ServicePlan newPlan) {
        this.servicePlan = newPlan;
    }
}
