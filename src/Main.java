public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    /*
    * private ActionListener createActionListener(String label) {
        return e -> {
            if (NumberleModel.gameIsOver) {
                return; // 直接返回，不执行任何操作
            }
            String transformedLabel = label;
            // 将UI使用的符号转换为Java识别的符号
            if ("×".equals(label)) {
                transformedLabel = "*";
            } else if ("÷".equals(label)) {
                transformedLabel = "/";
            }
            if ("Delete".equals(transformedLabel)) {
                if (currentCol > 0) {
                    matrix[currentRow][--currentCol] = ""; // 删除当前行的最后一个字符
                    isEnterPressed = false; // 重置 Enter 标志
                } else if (currentRow > 0 && !isEnterPressed) {
                    // 如果当前列为 0，且不在第一行，且未按下 Enter 移动到这一行，不执行删除
                    showLimitDialog("Cannot delete previous row!");
                }
            } else if ("Enter".equals(transformedLabel)) {
                if (currentCol == cols) { // 如果当前行已满
                    if (currentRow < rows - 1) { // 并且不是最后一行
                        currentRow++;
                        currentCol = 0;
                        if ((model.isValidEquation(model.arrayToString(matrix[currentRow - 1]))) == true) {
                            model.compareEquations(NumberleModel.arrayToString(matrix[currentRow - 1]), model.targetEquation);

                            model.incrementAttempts();
                            isEnterPressed = true; // 标记已经按下 Enter
                        } else {
                            currentRow--;
                            currentCol = 7;
                            showLimitDialog("Invalid Equation!");
                        }

                    } else {
                        if (model.attempts >= 6&&(model.isValidEquation(model.arrayToString(matrix[currentRow - 1]))) == true) {
                            showLimitDialog("GameOver!You Lose!"); // 已达到最大行数限制
                        } else {
                            if ((model.isValidEquation(model.arrayToString(matrix[currentRow - 1]))) == true) {
                                model.compareEquations(NumberleModel.arrayToString(matrix[currentRow - 1]), model.targetEquation);
                                model.incrementAttempts();
                                isEnterPressed = true; // 标记已经按下 Enter
                                System.out.println(model.compareEquations(NumberleModel.arrayToString(matrix[currentRow - 1]), model.targetEquation));
                                if(model.compareEquations(NumberleModel.arrayToString(matrix[currentRow - 1]), model.targetEquation)=="Win"){
                                    showLimitDialog("GameOver!You Win!");
                                        return;

                                }else if (model.compareEquations(NumberleModel.arrayToString(matrix[currentRow - 1]), model.targetEquation)=="Continue") {
                                    showLimitDialog("You have " + (6 - model.attempts) + " chances left.");
                                } else {
                                        // 如果游戏已结束，则不执行任何操作
                                        showLimitDialog("GameOver!You Lose!");
                                        return;
                                }
                            } else {
                                showLimitDialog("Invalid Equation!");
                            }
                        }

                    }
                } else if (currentCol == 0) { // 如果当前行未填充任何字符
                    showLimitDialog("Too Short!"); // 输入过短
                } else {
                    // 如果当前行未满且已有输入，显示输入过短的提示
                    showLimitDialog("Too Short!");
                }
            } else {
                if (currentCol < cols) { // 如果当前行未满
                    matrix[currentRow][currentCol++] = transformedLabel; // 添加输入到当前位置
                    isEnterPressed = false; // 重置 Enter 标志
                } else {
                    showLimitDialog("Input limit reached"); // 当前行已满，显示限制对话框
                }
            }
            repaint(); // 重绘以显示更新
        };
    }
    * */
}



