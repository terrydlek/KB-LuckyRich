<template>
    <div>
        <div id="element-to-print" style="padding: 20px; border: 1px solid #ccc;">
            <h1>PDF 테스트</h1>
            <p>asdsa</p>
            <p>dwqoijcqwdiojcwdqoijcdwqoijo</p>
        </div>
        <button @click="generatePDF">PDF 다운로드</button>
    </div>
</template>

<script>
import html2pdf from "html2pdf.js";

export default {
    methods: {
        generatePDF() {
            const element = document.getElementById("element-to-print");
            html2pdf()
                .from(element)
                .save("download.pdf")
                .then(() => {
                    console.log("PDF가 다운로드되었습니다.");
                })
                .catch((error) => {
                    console.error("PDF 다운로드 중 오류 발생:", error);
                });
        },
    },
};
</script>
