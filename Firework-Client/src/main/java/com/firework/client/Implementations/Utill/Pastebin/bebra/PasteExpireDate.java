package com.firework.client.Implementations.Utill.Pastebin.bebra;


public enum PasteExpireDate {
    NEVER("N", -1), TEN_MINUTES("10M", 10 * 60), ONE_HOUR("1H", 60 * 60), ONE_DAY(
            "1D", 60 * 60 * 24), ONE_WEEK("1W", 60 * 60 * 24 * 7), TWO_WEEKS(
            "2W", 60 * 60 * 24 * 14), ONE_MONTH("1M", -1);

    private String val;
    private int timeSeconds;

    /**
     * Creates a new <code>PasteExpireDate</code> instance.
     *
     * @param val
     *            a valid expire date value
     */
    PasteExpireDate(String val, int timeSeconds) {
        this.val = val;
        this.timeSeconds = timeSeconds;
    }

    /**
     * Get's the valid value for the 'api_paste_expire_date' parameter
     *
     * @return the valid value for the 'api_paste_expire_date' parameter
     */
    public String getValue() {
        return val;
    }

    /**
     * Gets PasteExpireDate based on: paste expire date minus paste date (in
     * seconds)
     *
     * @param timeSeconds
     *            seconds between expire date and paste date
     * @return PasteExpireDate
     */
    public static PasteExpireDate getExpireDate(int timeSeconds) {
        for (PasteExpireDate date : PasteExpireDate.values()) {
            if (date.timeSeconds == timeSeconds) {
                return date;
            }
        }
        return ONE_MONTH;
    }

}