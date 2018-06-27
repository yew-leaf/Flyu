package us.xingkong.flyu.activity.profile;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import us.xingkong.flyu.R;
import us.xingkong.flyu.UserModel;
import us.xingkong.flyu.UserModelDao;
import us.xingkong.flyu.base.BasePresenterImpl;
import us.xingkong.flyu.model.BmobUserModel;
import us.xingkong.flyu.util.L;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/8 11:40
 * @描述:
 * @更新日志:
 */
public class ProfilePresenter extends BasePresenterImpl<ProfileContract.View>
        implements ProfileContract.Presenter {

    private UserModelDao dao;

    ProfilePresenter(@NonNull ProfileContract.View view) {
        super(view);
        //dao = App.getInstance().getDaoSession().getUserModelDao();
    }

    @Override
    public void updateAvatar(final BmobUserModel bmobUserModel) {
        bmobUserModel.getAvatar().uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    bmobUserModel.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                mView.showMessage("修改成功");
                                mView.displayProfile(bmobUserModel);
                            } else {
                                L.e("ProfileFragment", e.getMessage());
                                L.e("ErrorCode", e.getErrorCode() + "");
                                mView.showMessage("修改失败：" + e.getMessage());
                            }
                        }
                    });
                } else {
                    L.e("ProfileFragment", e.getMessage());
                    L.e("ErrorCode", e.getErrorCode() + "");
                    mView.showMessage("上传失败：" + e.getMessage());
                }
                mView.setEnable(true);
            }

            @Override
            public void onProgress(Integer value) {
                super.onProgress(value);
                L.i("progress", value + "");
            }
        });
    }

    @Override
    public void updateSignature(final BmobUserModel bmobUserModel, final String sign) {
        final EditText editText = new EditText(mView.getActivity());
        editText.setText(sign);
        editText.setSelection(sign.length());
        editText.requestFocus();
        new AlertDialog.Builder(mView.getActivity())
                .setTitle(R.string.signature)
                .setView(editText)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (editText.getText().toString().equals(sign)) {
                            return;
                        }
                        bmobUserModel.setValue("signature", editText.getText().toString());
                        bmobUserModel.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    dialog.dismiss();
                                    mView.showMessage("修改成功");
                                    mView.showSignature(editText.getText().toString());
                                } else {
                                    L.e("ProfileFragment", e.getMessage());
                                    L.e("ErrorCode", e.getErrorCode() + "");
                                    mView.showMessage("修改失败：" + e.getMessage());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void logout(final UserModel userModel) {
        new AlertDialog.Builder(mView.getActivity())
                .setTitle("")
                .setMessage(R.string.do_you_want_to_exit)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*userModel.setIsLogged(false);
                        dao.update(userModel);*/
                        BmobUser.logOut();
                        mView.toOtherActivity(userModel);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void subscribe() {
        super.subscribe();
        if (!mView.isActive()) {
            return;
        }
        //mView.displayProfile(ContainerActivity.getBmobUserModel());
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
    }
}
