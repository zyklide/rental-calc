package protect.rentalcalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.common.collect.ImmutableMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowToast;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class PropertySummaryActivityTest
{
    private DBHelper db;

    private final Map<Integer, String> fieldNameLookup = new ImmutableMap.Builder<Integer, String>()
            .put(R.id.priceValue, "priceValue")
            .put(R.id.financedValue, "financedValue")
            .put(R.id.downPaymentValue, "downPaymentValue")
            .put(R.id.purchaseCostsValue, "purchaseCostsValue")
            .put(R.id.repairRemodelCostsValue, "repairRemodelCostsValue")
            .put(R.id.totalCashNeededValue, "totalCashNeededValue")
            .put(R.id.pricePerSizeValue, "pricePerSizeValue")
            .put(R.id.rentValue, "rentValue")
            .put(R.id.vancancyValue, "vancancyValue")
            .put(R.id.operatingIncomeValue, "operatingIncomeValue")
            .put(R.id.operatingExpensesValue, "operatingExpensesValue")
            .put(R.id.netOperatingIncomeValue, "netOperatingIncomeValue")
            .put(R.id.mortgageValue, "mortgageValue")
            .put(R.id.cashFlowValue, "cashFlowValue")
            .put(R.id.capitalizationRateValue, "capitalizationRateValue")
            .put(R.id.cashOnCashValue, "cashOnCashValue")
            .put(R.id.rentToValueValue, "rentToValueValue")
            .put(R.id.grossRentMultiplierValue, "grossRentMultiplierValue")
            .build();

    @Before
    public void setUp()
    {
        // Output logs emitted during tests so they may be accessed
        ShadowLog.stream = System.out;
        db = new DBHelper(RuntimeEnvironment.application);
    }

    @After
    public void tearDown()
    {
        db.close();
    }

    @Test
    public void clickBackFinishes()
    {
        ActivityController controller = startWithProperty(new Property());
        Activity activity = (Activity)controller.get();

        controller.start();
        controller.visible();
        controller.resume();

        assertTrue(shadowOf(activity).isFinishing() == false);
        shadowOf(activity).clickMenuItem(android.R.id.home);
        assertTrue(shadowOf(activity).isFinishing());

        // Check that can finish out the lifecycle
        controller.pause();
        controller.stop();
        controller.destroy();
    }

    @Test
    public void startWithoutBundle()
    {
        ActivityController controller = Robolectric.buildActivity(PropertySummaryActivity.class).create();
        Activity activity = (Activity)controller.get();

        controller.start();
        controller.visible();
        controller.resume();
        assertTrue(activity.isFinishing());

        String latestToast = ShadowToast.getTextOfLatestToast();
        assertNotNull(latestToast);
    }

    @Test
    public void startWithoutProperty()
    {
        Intent intent = new Intent();
        final Bundle bundle = new Bundle();
        intent.putExtras(bundle);

        ActivityController controller = Robolectric.buildActivity(PropertySummaryActivity.class, intent).create();
        Activity activity = (Activity)controller.get();

        controller.start();
        controller.visible();
        controller.resume();
        assertTrue(activity.isFinishing());

        String latestToast = ShadowToast.getTextOfLatestToast();
        assertNotNull(latestToast);
    }

    @Test
    public void startWithMissingProperty()
    {
        Intent intent = new Intent();
        final Bundle bundle = new Bundle();
        bundle.putLong("id", 0);
        intent.putExtras(bundle);

        ActivityController controller = Robolectric.buildActivity(PropertySummaryActivity.class, intent).create();
        Activity activity = (Activity)controller.get();

        controller.start();
        controller.visible();
        controller.resume();
        assertTrue(activity.isFinishing());

        String latestToast = ShadowToast.getTextOfLatestToast();
        assertNotNull(latestToast);
    }

    private ActivityController startWithProperty(Property property)
    {
        long id = db.insertProperty(property);

        Intent intent = new Intent();
        final Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        intent.putExtras(bundle);

        ActivityController controller = Robolectric.buildActivity(PropertySummaryActivity.class, intent).create();
        Activity activity = (Activity)controller.get();

        controller.start();
        controller.visible();
        controller.resume();
        assertTrue(activity.isFinishing() == false);

        String latestToast = ShadowToast.getTextOfLatestToast();
        assertNull(latestToast);

        return controller;
    }

    private void checkFields(Activity activity, Map<Integer, String> expected)
    {
        for(Map.Entry<Integer, String> item : expected.entrySet())
        {
            TextView view = (TextView)activity.findViewById(item.getKey());
            String text = view.getText().toString();
            String fieldName = fieldNameLookup.get(item.getKey());

            assertEquals(fieldName + " not as expected", item.getValue(), text);
        }
    }

    @Test
    public void startWithDefaultProperty() throws Exception
    {
        Property property = new Property();

        ActivityController controller = startWithProperty(property);
        Activity activity = (Activity)controller.get();

        Map<Integer, String> expectedValues = new ImmutableMap.Builder<Integer, String>()
                .put(R.id.priceValue, "0")
                .put(R.id.financedValue, "0")
                .put(R.id.downPaymentValue, "0")
                .put(R.id.purchaseCostsValue, "0")
                .put(R.id.repairRemodelCostsValue, "0")
                .put(R.id.totalCashNeededValue, "0")
                .put(R.id.pricePerSizeValue, "0")
                .put(R.id.rentValue, "0")
                .put(R.id.vancancyValue, "0")
                .put(R.id.operatingIncomeValue, "0")
                .put(R.id.operatingExpensesValue, "0")
                .put(R.id.netOperatingIncomeValue, "0")
                .put(R.id.mortgageValue, "0")
                .put(R.id.cashFlowValue, "0")
                .put(R.id.capitalizationRateValue, "0.0")
                .put(R.id.cashOnCashValue, "0.0")
                .put(R.id.rentToValueValue, "0.0")
                .put(R.id.grossRentMultiplierValue, "0.0")
                .build();

        checkFields(activity, expectedValues);
    }

    @Test
    public void startWithExampleProperty() throws Exception
    {
        Property property = new Property();

        property.purchasePrice = 123456;
        property.downPayment = 10;
        property.purchaseCosts = 4;
        property.repairRemodelCosts = 25000;
        property.propertySqft = 2342;
        property.grossRent = 1650;
        property.vacancy = 5;
        property.expenses = 15;
        property.interestRate = 5.125;
        property.loanDuration = 30;

        ActivityController controller = startWithProperty(property);
        Activity activity = (Activity)controller.get();

        Map<Integer, String> expectedValues = new ImmutableMap.Builder<Integer, String>()
                .put(R.id.priceValue, "123456")
                .put(R.id.financedValue, "111110")  // 90% of 123456
                .put(R.id.downPaymentValue, "12346")  // 10% of 123456 = 12345.6
                .put(R.id.purchaseCostsValue, "4938") // 4% of 123456 = 4938.24
                .put(R.id.repairRemodelCostsValue, "25000")
                .put(R.id.totalCashNeededValue, "42284") // 12345.6 + 4928.24 + 25000 = 42273.84
                .put(R.id.pricePerSizeValue, "53") // 123456/2342
                .put(R.id.rentValue, "1650")
                .put(R.id.vancancyValue, "83") // 5% of 1650 = 82.5
                .put(R.id.operatingIncomeValue, "1568")  // 1650 - 82.5
                .put(R.id.operatingExpensesValue, "248") // 15% of 1650 = 247.5
                .put(R.id.netOperatingIncomeValue, "1320") // 1650 - 82.5 - 247.5
                .put(R.id.mortgageValue, "605") // 604.98
                .put(R.id.cashFlowValue, "715") // 1650 - 82.5 - 247.5 - 604.98 = 715
                .put(R.id.capitalizationRateValue, "12.8") // 1320 * 12 / 123456 = 0.128
                .put(R.id.cashOnCashValue, "20.3") // 715*12 / 42273.84 = 0.2029
                .put(R.id.rentToValueValue, "1.3") // 1650 / 123456 = 0.0133
                .put(R.id.grossRentMultiplierValue, "6.2") // 123456 / (1650*12) = 6.23
                .build();

        checkFields(activity, expectedValues);
    }
}