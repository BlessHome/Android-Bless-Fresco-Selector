package com.bless.fresco.selector;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bless.fresco.selector.adapter.FolderListAdapter;
import com.bless.fresco.selector.adapter.ImageListAdapter;
import com.bless.fresco.selector.adapter.PreviewAdapter;
import com.bless.fresco.selector.bean.Folder;
import com.bless.fresco.selector.bean.Image;
import com.bless.fresco.selector.common.ImageSelectCallback;
import com.bless.fresco.selector.common.ImageSelectConstant;
import com.bless.fresco.selector.common.OnFolderChangeListener;
import com.bless.fresco.selector.common.OnItemClickListener;
import com.bless.fresco.selector.utils.FileUtils;
import com.bless.fresco.selector.widget.CustomViewPager;
import com.bless.fresco.selector.widget.DividerGridItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *
 * 作者:      ASLai(gdcpljh@126.com).
 * 日期:      17-7-14
 * 版本:      V1.0
 * 描述:      图像选择
 *
 * </pre>
 */

public class ImageSelectFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = ImageSelectFragment.class.getSimpleName();
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private static final int REQUEST_CAMERA = 5;
    private static final int CAMERA_REQUEST_CODE = 1;
    private RecyclerView _Imageselectimagelistrecyclerview;
    private Button _ImageSelectAlbumselectedButton;
    private View _ImageSelectBottomRelativelayout;
    private CustomViewPager _ImageSelectImageViewpager;
    private ImageSelectConfig config;
    private ImageSelectCallback imageSelectCallback;
    private List<Folder> folderList = new ArrayList<>();
    private List<Image> imageList = new ArrayList<>();
    private ListPopupWindow folderPopupWindow;
    private ImageListAdapter imageListAdapter;
    private FolderListAdapter folderListAdapter;
    private PreviewAdapter previewAdapter;
    private boolean hasFolderGened = false;
    private File tempFile;
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    List<Image> tempImageList = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        if (!image.path.endsWith("gif"))
                            tempImageList.add(image);
                        if (!hasFolderGened) {
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            if (folderFile == null) {
                                System.out.println(path);
                                return;
                            }
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!folderList.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                folderList.add(folder);
                            } else {
                                Folder f = folderList.get(folderList.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    imageList.clear();
                    if (config.isNeedCamera())
                        imageList.add(new Image());
                    imageList.addAll(tempImageList);


                    imageListAdapter.notifyDataSetChanged();

                    if (ImageSelectConstant.imageList != null && ImageSelectConstant.imageList.size() > 0) {
                        //imageListAdapter.setDefaultSelected(Constant.imageList);
                    }

                    folderListAdapter.notifyDataSetChanged();

                    hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    public static ImageSelectFragment instance() {
        ImageSelectFragment fragment = new ImageSelectFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_select_fragment, container, false);
        _Imageselectimagelistrecyclerview = (RecyclerView) view.findViewById(R.id.image_select_imageList_recyclerView);
        _ImageSelectAlbumselectedButton = (Button) view.findViewById(R.id.image_select_albumSelected_button);
        _ImageSelectAlbumselectedButton.setOnClickListener(this);
        _ImageSelectBottomRelativelayout = view.findViewById(R.id.image_select_bottom_relativeLayout);
        _ImageSelectImageViewpager = (CustomViewPager) view.findViewById(R.id.image_select_image_viewPager);
        _ImageSelectImageViewpager.addOnPageChangeListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        config = ImageSelectConstant.config;
        try {
            imageSelectCallback = (ImageSelectCallback) getActivity();
        } catch (Exception e) {

        }

        _ImageSelectAlbumselectedButton.setText(config.getAllImagesText());

        _Imageselectimagelistrecyclerview.setLayoutManager(new GridLayoutManager(_Imageselectimagelistrecyclerview.getContext(), 3));
        _Imageselectimagelistrecyclerview.addItemDecoration(new DividerGridItemDecoration(_Imageselectimagelistrecyclerview.getContext()));
        if (config.isNeedCamera())
            imageList.add(new Image());

        imageListAdapter = new ImageListAdapter(getActivity(), imageList, config);
        _Imageselectimagelistrecyclerview.setAdapter(imageListAdapter);
        imageListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public int onCheckedClick(int position, Image image) {
                return checkedImage(position, image);
            }

            @Override
            public void onImageClick(int position, Image image) {
                if (config.isNeedCamera() && position == 0) {
                    showCameraAction();
                } else {
                    if (config.isMultiSelect()) {
                        _ImageSelectImageViewpager.setAdapter((previewAdapter = new PreviewAdapter(getActivity(), imageList, config)));
                        previewAdapter.setListener(new OnItemClickListener() {
                            @Override
                            public int onCheckedClick(int position, Image image) {
                                return checkedImage(position, image);
                            }

                            @Override
                            public void onImageClick(int position, Image image) {
                                hidePreview();
                            }
                        });
                        if (config.isNeedCamera()) {
                            imageSelectCallback.onPreviewChanged(position, imageList.size() - 1, true);
                        } else {
                            imageSelectCallback.onPreviewChanged(position + 1, imageList.size(), true);
                        }
                        _ImageSelectImageViewpager.setCurrentItem(config.isNeedCamera() ? position - 1 : position);
                        _ImageSelectImageViewpager.setVisibility(View.VISIBLE);
                    } else {
                        if (imageSelectCallback != null) {
                            imageSelectCallback.onSingleImageSelected(image.path);
                        }
                    }
                }
            }
        });

        folderListAdapter = new FolderListAdapter(getActivity(), folderList, config);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    private int checkedImage(int position, Image image) {
        if (image != null) {
            if (ImageSelectConstant.imageList.contains(image.path)) {
                ImageSelectConstant.imageList.remove(image.path);
                if (imageSelectCallback != null) {
                    imageSelectCallback.onImageUnselected(image.path);
                }
            } else {
                if (config.getMaxNum() <= ImageSelectConstant.imageList.size()) {
                    Toast.makeText(getActivity(), String.format(getString(R.string.image_select_text_maxNum), config.getMaxNum()), Toast.LENGTH_SHORT).show();
                    return 0;
                }

                ImageSelectConstant.imageList.add(image.path);
                if (imageSelectCallback != null) {
                    imageSelectCallback.onImageSelected(image.path);
                }
            }
            return 1;
        }
        return 0;
    }

    private void createPopupFolderList(int width, int height) {
        folderPopupWindow = new ListPopupWindow(getActivity());
        folderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#aaaaaa")));
        folderPopupWindow.setAdapter(folderListAdapter);
        folderPopupWindow.setContentWidth(width);
        folderPopupWindow.setWidth(width);
        folderPopupWindow.setHeight(height);
        folderPopupWindow.setAnchorView(_ImageSelectBottomRelativelayout);
        folderPopupWindow.setModal(true);
        folderListAdapter.setOnFloderChangeListener(new OnFolderChangeListener() {
            @Override
            public void onChange(int position, Folder folder) {
                folderPopupWindow.dismiss();
                if (position == 0) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    _ImageSelectAlbumselectedButton.setText(config.getAllImagesText());
                } else {
                    imageList.clear();
                    if (config.isNeedCamera())
                        imageList.add(new Image());
                    imageList.addAll(folder.images);
                    imageListAdapter.notifyDataSetChanged();

                    _ImageSelectAlbumselectedButton.setText(folder.name);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == _ImageSelectAlbumselectedButton.getId()) {
            if (folderPopupWindow == null) {
                WindowManager wm = getActivity().getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                createPopupFolderList(width / 3 * 2, width / 3 * 2);
            }

            if (folderPopupWindow.isShowing()) {
                folderPopupWindow.dismiss();
            } else {
                folderPopupWindow.show();
                if (folderPopupWindow.getListView() != null) {
                    folderPopupWindow.getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.image_select_bottom_bg)));
                }
                int index = folderListAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                folderPopupWindow.getListView().setSelection(index);
            }
        }
    }

    private void showCameraAction() {

        if (config.getMaxNum() <= ImageSelectConstant.imageList.size()) {
            Toast.makeText(getActivity(), String.format(getString(R.string.image_select_text_maxNum), config.getMaxNum()), Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            return;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            tempFile = new File(config.getFolderPath(), System.currentTimeMillis() + ".jpg");
            Log.e(TAG, tempFile.getAbsolutePath());
            FileUtils.createFile(tempFile);

            Uri uri = FileProvider.getUriForFile(
                    getActivity(),
                    FileUtils.getApplicationId(getActivity()) + ".provider",
                    tempFile);

            List<ResolveInfo> resInfoList = getActivity().getPackageManager()
                    .queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //Uri.fromFile(tempFile)
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getActivity(), getString(R.string.image_select_text_open_camera_failure), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (tempFile != null) {
                    if (imageSelectCallback != null) {
                        imageSelectCallback.onCameraShot(tempFile);
                    }
                }
            } else {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCameraAction();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.image_select_text_permission_camera_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (config.isNeedCamera()) {
            imageSelectCallback.onPreviewChanged(position + 1, imageList.size() - 1, true);
        } else {
            imageSelectCallback.onPreviewChanged(position + 1, imageList.size(), true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public boolean hidePreview() {
        if (_ImageSelectImageViewpager.getVisibility() == View.VISIBLE) {
            _ImageSelectImageViewpager.setVisibility(View.GONE);
            imageSelectCallback.onPreviewChanged(0, 0, false);
            imageListAdapter.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }
}
