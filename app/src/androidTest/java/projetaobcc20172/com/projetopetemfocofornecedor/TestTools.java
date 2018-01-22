package projetaobcc20172.com.projetopetemfocofornecedor;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import java.util.Random;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Created by raul on 06/12/17.
 * Essa classe é responsavel por armazenar os métodos que são utilizados nos testes do espresso
 * que independem da activity que está sendo utilizada.
 */

public class TestTools {
    //Verifica se a mensagem do toast é a correta
    public static void checarToast(int rMsg){
        Activity act = TestTools.activityAtual();
        Espresso.onView(ViewMatchers.withText(act.getResources().getString(rMsg))).inRoot(withDecorView(not(is(act.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    public static  void checarToast(String msg){
        Activity act = TestTools.activityAtual();
        Espresso.onView(ViewMatchers.withText(msg)).inRoot(withDecorView(not(is(act.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //Verifica se a mudança de activity(tela) ocorreu como esperada
    public static void verificarMudancaActivity(String nomeActivity){
        intended(hasComponent(nomeActivity));
        Intents.release();
    }

    //Recebe a R.id.campoTexto e o texto a ser inserido no teste
    public static void digitarCampo(int idCampo,String textoAserDigitado){
        Espresso.onView(withId(idCampo)).perform(clearText(),ViewActions.typeText(textoAserDigitado));
        Espresso.closeSoftKeyboard();
    }

    public static void digitarCampoComScroll(int idCampo,String textoAserDigitado){
        Espresso.onView(ViewMatchers.withId(idCampo)).perform(ViewActions.scrollTo(),clearText(),ViewActions.typeText(textoAserDigitado));
        Espresso.closeSoftKeyboard();
    }
    public static void clicarBotao(int idBotao){
        Espresso.onView(ViewMatchers.withId(idBotao)).perform(ViewActions.click());
    }
    //Caso a tela contenha scroll e o botão não estaja visivel é necessario
    //rolar a barra até ele para depois clicar
    public static void clicarBotaoComScroll(int idBotao){
        Espresso.onView(ViewMatchers.withId(idBotao)).perform(ViewActions.scrollTo(),ViewActions.click());
    }
    public static void selecionarSpinner(int idSpinner,String textoSpinner){
        Espresso.onView(ViewMatchers.withId(idSpinner)).perform(ViewActions.click());
        Espresso.onData(allOf(is(instanceOf(String.class)),is(textoSpinner))).perform(ViewActions.click());
    }

    public static void selecionarItemSpinnerComScroll(int idSpinner,String textoSpinner){
        Espresso.onView(ViewMatchers.withId(idSpinner)).perform(ViewActions.scrollTo(),ViewActions.click());
        Espresso.onData(allOf(is(instanceOf(String.class)),is(textoSpinner))).perform(ViewActions.click());
    }
    //Retorna activity que está atualmente instanciada
    public static  Activity activityAtual() {
        final Activity[] currentActivity = new Activity[1];
        Espresso.onView(allOf(ViewMatchers.withId(android.R.id.content), isDisplayed())).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view.getContext() instanceof Activity) {
                    Activity activity1 = ((Activity)view.getContext());
                    currentActivity[0] = activity1;
                }
            }
        });
        return currentActivity[0];
    }

    //Gera um email aleatorio para realizar cadastro
    public static String gerarEmailTeste(int num){
        int x;
        Random ran = new Random();
        String emailInicio = "teste";
        String emailFim = "@gmail.com";
        for(int i=0;i<=num;i=i+1){
            x = ran.nextInt(50);
            emailInicio = emailInicio + x;
        }
        return emailInicio+emailFim;
    }

    //Lança um erro de teste se dois valores não forem iguais
    public static void verQtElementos(int qtEncontrada,int qtEsperada){
        assertEquals(qtEncontrada,qtEsperada);
    }

    public static void qtNaoNula(int qtEncontrada){
        assertTrue(qtEncontrada>0);
    }

    public static void pressionarBuscarTeclado(){
        Instrumentation inst = new Instrumentation();
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_SEARCH);
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
    }

    public static void checarListViewComTextView(Activity act,int idListView,int idTextView,String textoSerBuscado,int tAmostra){
        ListView list =  act.findViewById(idListView);
        TextView textView;
        String texto;
        int amostra = tAmostra;
        if(amostra>list.getCount()){
            amostra = list.getCount();
        }
        for(int i = 0;i<amostra;i++){
            textView = list.getChildAt(i).findViewById(idTextView);
            texto = textView.getText().toString();
            assertTrue(texto.contains(textoSerBuscado));
        }
    }

    public static void checarTamanhoList(Activity act,int idListView,int qtEsperada){
        ListView list =  act.findViewById(idListView);
        assertEquals(list.getCount(),qtEsperada);
    }

    public static void clicarEmITemListView(int idView,int indice){
        onData(anything()).inAdapterView(withId(idView)).atPosition(indice).perform(click());
    }

    public static void clicarEmItemDentroListView(int idView,int indice,int idItem){
        onData(anything())
                .inAdapterView(withId(idView))
                .atPosition(indice)
                .onChildView(withId(idItem))
                .perform(click());
    }

    public static int getTamanhoListView(Activity act,int idListView){
        ListView list =  act.findViewById(idListView);
        return list.getCount();
    }




    public static void login(String mEmail, String mSenha){
        ViewInteraction appCompatEditText = onView(
                Matchers.allOf(withId(R.id.etLoginEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                4)));
        appCompatEditText.perform(scrollTo(), replaceText(mEmail), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                Matchers.allOf(withId(R.id.etLoginSenha),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                7)));
        appCompatEditText2.perform(scrollTo(), replaceText(mSenha), closeSoftKeyboard());
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                Matchers.allOf(withId(R.id.btnLogin), withText("Entrar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                9)));
        appCompatButton.perform(scrollTo(), click());
    }

    public static void clicarItemMenuEditar(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                Matchers.allOf(withId(R.id.title), withText("Editar Perfil"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(Matchers.is("android.support.v7.view.menu.ListMenuItemView")),
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

}
