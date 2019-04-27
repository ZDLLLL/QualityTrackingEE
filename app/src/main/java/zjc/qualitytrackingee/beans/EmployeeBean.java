package zjc.qualitytrackingee.beans;

import java.util.List;

public class EmployeeBean {
    private List<Employee> employee;

    public List<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(List<Employee> employee) {
        this.employee = employee;
    }
//    public Employee getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(Employee employee) {
//        this.employee = employee;
//    }

    public class Employee{
        private String e_id;
        private String e_passowrd;
        private String e_name;
        private String e_power;
        private String e_phone;
        private String e_job;
        private String e_status;
        private String e_img;
        private String c_id;

        public String getE_id() {
            return e_id;
        }

        public void setE_id(String e_id) {
            this.e_id = e_id;
        }

        public String getE_passowrd() {
            return e_passowrd;
        }

        public void setE_passowrd(String e_passowrd) {
            this.e_passowrd = e_passowrd;
        }

        public String getE_name() {
            return e_name;
        }

        public void setE_name(String e_name) {
            this.e_name = e_name;
        }

        public String getE_power() {
            return e_power;
        }

        public void setE_power(String e_power) {
            this.e_power = e_power;
        }

        public String getE_phone() {
            return e_phone;
        }

        public void setE_phone(String e_phone) {
            this.e_phone = e_phone;
        }

        public String getE_job() {
            return e_job;
        }

        public void setE_job(String e_job) {
            this.e_job = e_job;
        }

        public String getE_status() {
            return e_status;
        }

        public void setE_status(String e_status) {
            this.e_status = e_status;
        }

        public String getE_img() {
            return e_img;
        }

        public void setE_img(String e_img) {
            this.e_img = e_img;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }
    }
}
