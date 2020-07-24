package com.qingpeng.mz.bean;

import java.util.List;

public class GuanLiBean {


    /**
     * data : {"ManagerList":[{"Account":"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3669707182,1525465329&fm=26&gp=0.jpg","NickName":"云知道","UserId":100022},{"Account":"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3669707182,1525465329&fm=26&gp=0.jpg","NickName":"云知道","UserId":100022},{"Account":"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3669707182,1525465329&fm=26&gp=0.jpg","NickName":"鱼之一","UserId":100026},{"Account":"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3669707182,1525465329&fm=26&gp=0.jpg","NickName":"鱼之一","UserId":100026}]}
     * info : 是管理
     * status : 1
     */

    private List<ManagerListBean> ManagerList;

    public List<ManagerListBean> getManagerList() {
        return ManagerList;
    }

    public void setManagerList(List<ManagerListBean> ManagerList) {
        this.ManagerList = ManagerList;
    }

    public static class ManagerListBean {
        /**
         * Account : https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3669707182,1525465329&fm=26&gp=0.jpg
         * NickName : 云知道
         * UserId : 100022
         */

        private String Account;
        private String NickName;
        private int UserId;

        public String getAccount() {
            return Account;
        }

        public void setAccount(String Account) {
            this.Account = Account;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }
    }
}
