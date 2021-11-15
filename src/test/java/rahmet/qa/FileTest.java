package rahmet.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileTest {

    @Test
    @DisplayName("Загрузка файла")
    void uploadFile() {
        open("http://www.csm-testcenter.org/test?do=show&subdo=common&test=file_upload");
        $("input[type='file']").uploadFromClasspath("maven.txt");
        $("#button").click();
        $(byText("Filename")).parent().shouldHave(text("maven.txt"));
    }

    @Test
    @DisplayName("Загрузка текстового файла и проверка содержимого")
    void downloadTxtFileAndCheck() throws IOException {
        open("https://github.com/selenide/selenide/blob/master/README.md");
        File txt = $("#raw-url").download();
        String txtContent = IOUtils.toString(new FileReader(txt));
        assertTrue(txtContent.contains("Selenide is based on and is compatible to Selenium WebDriver 4.0+"));
    }

    @Test
    @DisplayName("Зарузка PDF файла и проверка количества страниц")
    void downloadPdfFileAndCheck() throws IOException{
        open("https://knowledge.autodesk.com/support/maya/getting-started/caas/simplecontent/content/maya-documentation.html");
        File pdf = $(byText("PyQt for Maya 2016")).download();
        PDF parsedPdf = new PDF(pdf);
        assertEquals(9, parsedPdf.numberOfPages);
    }

    @Test
    @DisplayName("Скачивание Excel файла и проверка значения")
    void downloadExcelFileAndCheck() throws IOException{
        open("https://file-examples.com/index.php/sample-documents-download/sample-xls-download/");
        File xls = $$("a[href*='file_example_XLS_10']")
                .find(text("Download sample xls file"))
                .download();

        XLS parsedXls = new XLS(xls);
        boolean checkCell = parsedXls.excel
                .getSheetAt(0)
                .getRow(5)
                .getCell(1)
                .getStringCellValue()
                .contains("Nereida");

        assertTrue(checkCell);
    }
}
