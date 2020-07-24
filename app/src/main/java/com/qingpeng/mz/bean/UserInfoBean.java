package com.qingpeng.mz.bean;

public class UserInfoBean {


    /**
     * data : {"info":{"UserId":100001,"NickName":"旺仔","Account":18503723336,"Token":"8995ba84e3c5afe86859c21c3d242296","Fans":0,"Followed":0,"Sex":0,"Address":"","Sign":"","Bank":{"Wx":"","WxName":"","Ali":"","AliName":"","CardName":"","BankName":"","BankCard":0,"Province":"","City":"","BankAddr":""},"LastIp":"192.168.0.109","Talk":0}}
     * info : 用户信息
     * status : 1
     */


    /**
     * info : {"UserId":100001,"NickName":"旺仔","Account":18503723336,"Token":"8995ba84e3c5afe86859c21c3d242296","Fans":0,"Followed":0,"Sex":0,"Address":"","Sign":"","Bank":{"Wx":"","WxName":"","Ali":"","AliName":"","CardName":"","BankName":"","BankCard":0,"Province":"","City":"","BankAddr":""},"LastIp":"192.168.0.109","Talk":0}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * UserId : 100001
         * NickName : 旺仔
         * Account : 18503723336
         * Token : 8995ba84e3c5afe86859c21c3d242296
         * Fans : 0
         * Followed : 0
         * Sex : 0
         * Address :
         * Sign :
         * Bank : {"Wx":"","WxName":"","Ali":"","AliName":"","CardName":"","BankName":"","BankCard":0,"Province":"","City":"","BankAddr":""}
         * LastIp : 192.168.0.109
         * Talk : 0
         */

        private int UserId;
        private String NickName;
        private long Account;
        private String Token;
        private int Fans;
        private int Followed;
        private int Sex;
        private String Address;
        private String Sign;
        private BankBean Bank;
        private String LastIp;
        private int Talk;

        public String getAvater() {
            return Avater;
        }

        public void setAvater(String avater) {
            Avater = avater;
        }

        private String Avater;

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public long getAccount() {
            return Account;
        }

        public void setAccount(long Account) {
            this.Account = Account;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public int getFans() {
            return Fans;
        }

        public void setFans(int Fans) {
            this.Fans = Fans;
        }

        public int getFollowed() {
            return Followed;
        }

        public void setFollowed(int Followed) {
            this.Followed = Followed;
        }

        public int getSex() {
            return Sex;
        }

        public void setSex(int Sex) {
            this.Sex = Sex;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getSign() {
            return Sign;
        }

        public void setSign(String Sign) {
            this.Sign = Sign;
        }

        public BankBean getBank() {
            return Bank;
        }

        public void setBank(BankBean Bank) {
            this.Bank = Bank;
        }

        public String getLastIp() {
            return LastIp;
        }

        public void setLastIp(String LastIp) {
            this.LastIp = LastIp;
        }

        public int getTalk() {
            return Talk;
        }

        public void setTalk(int Talk) {
            this.Talk = Talk;
        }

        public static class BankBean {
            /**
             * Wx :
             * WxName :
             * Ali :
             * AliName :
             * CardName :
             * BankName :
             * BankCard : 0
             * Province :
             * City :
             * BankAddr :
             */

            private String Wx;
            private String WxName;
            private String Ali;
            private String AliName;
            private String CardName;
            private String BankName;
            private int BankCard;
            private String Province;
            private String City;
            private String BankAddr;

            public String getWx() {
                return Wx;
            }

            public void setWx(String Wx) {
                this.Wx = Wx;
            }

            public String getWxName() {
                return WxName;
            }

            public void setWxName(String WxName) {
                this.WxName = WxName;
            }

            public String getAli() {
                return Ali;
            }

            public void setAli(String Ali) {
                this.Ali = Ali;
            }

            public String getAliName() {
                return AliName;
            }

            public void setAliName(String AliName) {
                this.AliName = AliName;
            }

            public String getCardName() {
                return CardName;
            }

            public void setCardName(String CardName) {
                this.CardName = CardName;
            }

            public String getBankName() {
                return BankName;
            }

            public void setBankName(String BankName) {
                this.BankName = BankName;
            }

            public int getBankCard() {
                return BankCard;
            }

            public void setBankCard(int BankCard) {
                this.BankCard = BankCard;
            }

            public String getProvince() {
                return Province;
            }

            public void setProvince(String Province) {
                this.Province = Province;
            }

            public String getCity() {
                return City;
            }

            public void setCity(String City) {
                this.City = City;
            }

            public String getBankAddr() {
                return BankAddr;
            }

            public void setBankAddr(String BankAddr) {
                this.BankAddr = BankAddr;
            }
        }
    }
}
