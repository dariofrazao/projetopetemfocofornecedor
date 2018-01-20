package projetaobcc20172.com.projetopetemfocofornecedor.editarfornecedortest;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.TestTools;
import projetaobcc20172.com.projetopetemfocofornecedor.activity.LoginActivity;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by renat on 19/01/2018.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditFornecedorActivityTest {

    //Essas variaveis guardam valores que são preenchidos nos campos
    private String mTelefone = "87992756888";
    private String mHorarios = "Tarde";
    private String mCampovazio = "";
    private String mEmail = "for@gmail.com";
    private String mSenha = "123456";

    @Before
    public void setUp() throws Exception {
        //Desloga caso já esteja logado.
        //Evita erros nos testes
        try{
            TestTools.clicarBotao(R.id.btnSair);
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Rule
    public ActivityTestRule<LoginActivity> editFornecedorActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test //testa editar fornecedor com todos os campo preenchidos
    public void editarFornecedorCamposPreenchidos() throws InterruptedException {
        login();
        clicarItemMenuEditar();
        TestToolsEditFornecedor.preencherEclicar(this.mTelefone, this.mHorarios);
        TestTools.checarToast(R.string.sucesso_atualizacao);
    }

    @Test //testa editar fornecedor com algum campo vazio
    public void editarFornecedorCamposNaoPreenchidos() throws InterruptedException {
        login();
        clicarItemMenuEditar();
        TestToolsEditFornecedor.preencherEclicar(this.mCampovazio, this.mHorarios);
        TestTools.checarToast(R.string.erro_editar_fornecedor_campos_obrigatorios_Toast);
    }

    public void login(){
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etLoginEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                4)));
        appCompatEditText.perform(scrollTo(), replaceText(mEmail), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.etLoginSenha),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                7)));
        appCompatEditText2.perform(scrollTo(), replaceText(mSenha), closeSoftKeyboard());
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnLogin), withText("Entrar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                9)));
        appCompatButton.perform(scrollTo(), click());
    }

    public void clicarItemMenuEditar(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Editar Perfil"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };

    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(5000);
    }
}